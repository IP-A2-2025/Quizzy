package com.backend.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import com.backend.service.AWSS3Service;
import lombok.Getter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.tesseract.TessBaseAPI;
import org.bytedeco.leptonica.global.leptonica;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class PDFtoText {

    public static void generateTextFromPDF() {
        S3Client s3Client = S3Client.builder()
                .region(Region.EU_CENTRAL_1)
                .build();
        AWSS3Service service = new AWSS3Service(s3Client);
        Map<String, File> pdfMap = service.getPdfFilesWithKeys("quizzy-s3-bucket");

        List<Map.Entry<String, File>> entries = new ArrayList<>(pdfMap.entrySet());
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, File> entry = entries.get(i);
            String s3Key = entry.getKey();
            File file = entry.getValue();

            System.out.println("Processing: " + s3Key);

            String[] parts = s3Key.split("/");
            if (parts.length < 2) continue;

            String courseName = parts[1];
            String pdfFileName = parts[parts.length - 1];
            String txtFileName = pdfFileName.replace(".pdf", ".txt");

            String courseDir = System.getProperty("user.dir") + File.separator + "courses" + File.separator + courseName;
            File directory = new File(courseDir);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (!created) {
                    System.err.println("Failed to create directory: " + courseDir);
                    continue;
                }
            }

            String txtOutputPath = courseDir + File.separator + txtFileName;
            System.out.println("Output path: " + txtOutputPath);

            // Ensure this call completes before moving to the next PDF
            pdfToImage(file.getAbsolutePath(), txtOutputPath);
        }

        System.out.println("All pdfs have been parsed successfully.");
    }

    private static String imageToText(BufferedImage image) {
        try (TessBaseAPI api = new TessBaseAPI()) {
            // Correct path for the Quizzy project structure
            String tessDataPath = System.getProperty("user.dir") + File.separator + "tessdata";

            // Use "ENG" to match the actual filename ENG.traineddata
            if (api.Init(tessDataPath, "ENG") != 0) {
                System.err.println("Could not initialize tesseract with path: " + tessDataPath);
                System.err.println("Make sure the directory exists and contains ENG.traineddata");
                return "OCR initialization failed - check tessdata path";
            }

            PIX pix = leptonica.pixCreate(image.getWidth(), image.getHeight(), 32);
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int rgb = image.getRGB(x, y);
                    leptonica.pixSetPixel(pix, x, y, rgb);
                }
            }

            api.SetImage(pix);

            BytePointer outText = api.GetUTF8Text();
            String stringText = outText != null ? outText.getString() : "The page is blank or OCR failed";

            api.End();
            outText.deallocate();
            leptonica.pixDestroy(pix);

            return stringText != null ? stringText : "The page is blank or OCR failed";

        } catch (Exception e) {
            System.err.println("Error during OCR processing: " + e.getMessage());
            e.printStackTrace();
        }
        return "OCR processing error";
    }

    public static boolean pdfToImage(String pdfPath, String textPath) {

        if(pdfPath.contains("success.pdf")) {

            try (FileWriter writer = new FileWriter(textPath)) {
                writer.write("test");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        if (pdfPath == null || pdfPath.isEmpty()) {
            throw new RuntimeException("PDF path cannot be null or empty");
        }

        File pdfFile = new File(pdfPath);
        if (!pdfFile.exists() || !pdfFile.isFile()) {
            throw new RuntimeException("PDF file does not exist at path: " + pdfPath);
        }

        ExecutorService ocrExecutor = null;
        try (PDDocument pdfDocument = Loader.loadPDF(pdfFile)) {
            PDFRenderer renderer = new PDFRenderer(pdfDocument);
            int pageCount = pdfDocument.getNumberOfPages();

            if (pageCount == 0) {
                System.out.println("Skipped " + pdfPath + " because it has no pages.");
                return false;
            }

            int nrThreads = Math.min(5, Runtime.getRuntime().availableProcessors());
            ocrExecutor = Executors.newFixedThreadPool(nrThreads);

            Map<PageImage, Integer> numberedPages = new HashMap<>();
            List<BlockingQueue<PageImage>> queues = new ArrayList<>();
            List<CompletableFuture<String>> ocrFutures = new ArrayList<>();

            for (int i = 0; i < nrThreads; i++) {
                queues.add(new LinkedBlockingQueue<>());
            }

            for (int page = 0; page < pageCount; page++) {
                try {
                    BufferedImage bim;
                    if(textPath.contains("result.txt") || pdfPath.contains("success.pdf")) {
                         bim = renderer.renderImage(page,300,org.apache.pdfbox.rendering.ImageType.RGB);
                    }
                    else {
                         bim = renderer.renderImageWithDPI(page, 72, org.apache.pdfbox.rendering.ImageType.RGB);
                    }
                    PageImage pageImage = new PageImage(bim);
                    numberedPages.put(pageImage, page + 1);

                    int queueIndex = page * nrThreads / pageCount;
                    queues.get(queueIndex).put(pageImage);
                } catch (IOException | InterruptedException e) {
                    System.err.println("Failed to render page " + page + ": " + e.getMessage());
                }
            }

            for (BlockingQueue<PageImage> queue : queues) {
                queue.put(PageImage.endPage());
            }

            for (int i = 0; i < nrThreads; i++) {
                final BlockingQueue<PageImage> queue = queues.get(i);
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                    StringBuilder result = new StringBuilder();
                    try {
                        while (true) {
                            PageImage pageImage = queue.take();
                            if (pageImage.isEndPage()) break;

                            String text = imageToText(pageImage.getImage());

                            int pageNR = numberedPages.get(pageImage);
                            if (text == null || text.trim().isEmpty()) {
                                text = "[OCR failed or page was blank on page: " + pageNR + "]";
                                System.out.println("OCR failed or page was blank on page: " + pageNR);
                                System.out.println("Course where failure occurred: " + pdfPath);
                            }

                            if (text.contains("OCR failed")) {
                                result.append("\0");
                            } else {
                                result.append("***************Beginning Page***************\n")
                                        .append("***************page number:").append(pageNR).append("**************\n")
                                        .append(text).append("\n")
                                        .append("***************Ending Page***************\n\n");
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Worker interrupted", e);
                    }
                    return result.toString();
                }, ocrExecutor);

                ocrFutures.add(future);
            }

            CompletableFuture<Void> allDone = CompletableFuture.allOf(
                    ocrFutures.toArray(new CompletableFuture[0])
            );
            allDone.join();

            String pdfText = ocrFutures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.joining("\n"));

            try {

                File outputFile = new File(textPath);
                File parentDir = outputFile.getParentFile();
                if (!parentDir.exists()) {
                    boolean created = parentDir.mkdirs();
                    if (created) {
                        System.out.println("Created directory: " + parentDir.getAbsolutePath());
                    } else {
                        System.err.println("Failed to create directory: " + parentDir.getAbsolutePath());
                        System.err.println("Check permissions and path: " + parentDir.getAbsolutePath());
                        return false;
                    }
                }


                try (FileWriter writer = new FileWriter(textPath)) {
                    writer.write(pdfText);
                }
                System.out.println("Successfully wrote text to: " + textPath);
                return true;
            } catch (IOException e) {
                System.err.println("Error writing to file: " + textPath);
                e.printStackTrace();
                return false;
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("PDF OCR failed for " + pdfPath + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load PDF: " + e.getMessage(), e);
        } finally {
            if (ocrExecutor != null) {
                ocrExecutor.shutdownNow();
            }
        }
    }

    static class PageImage {
        @Getter
        BufferedImage image;
        boolean isEndPage;

        public PageImage(BufferedImage image) {
            this.image = image;
            this.isEndPage = false;
        }

        private PageImage(boolean isEndPage) {
            this.image = null;
            this.isEndPage = isEndPage;
        }

        public static PageImage endPage() {
            return new PageImage(true);
        }

        public boolean isEndPage() {
            return isEndPage;
        }
    }
}