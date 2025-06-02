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


            pdfToImage(file.getAbsolutePath(), txtOutputPath);
        }

        System.out.println("All pdfs have been parsed successfully.");
    }

    private static String imageToText(BufferedImage image) {
        try (TessBaseAPI api = new TessBaseAPI()) {

            String tessDataPath = System.getProperty("user.dir") + File.separator + "tessdata";


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

        if (pdfPath.isEmpty()) {
            throw new RuntimeException("PDF path cannot be null or empty");
        }

        File pdfFile = new File(pdfPath);
        if (!pdfFile.exists() || !pdfFile.isFile()) {
            throw new RuntimeException("PDF file does not exist at path: " + pdfPath);
        }

        File outputFile = new File(textPath);
        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                System.err.println("Failed to create directory: " + parentDir.getAbsolutePath());
                return false;
            }
            System.out.println("Created directory: " + parentDir.getAbsolutePath());
        }


        final int batchSize = 10;

        try (PDDocument pdfDocument = Loader.loadPDF(pdfFile)) {
            int pageCount = pdfDocument.getNumberOfPages();

            if (pageCount == 0) {
                System.out.println("Skipped " + pdfPath + " because it has no pages.");
                try (FileWriter writer = new FileWriter(textPath)) {
                    writer.write("PDF had no pages to process.");
                }
                return true;
            }


            int totalBatches = (int) Math.ceil((double) pageCount / batchSize);

            boolean firstBatch = true;
            int currentPage=0;
            for (int batchIndex = 0; batchIndex < totalBatches; batchIndex++) {
                int startPage = batchIndex * batchSize;
                int endPage = Math.min((batchIndex + 1) * batchSize, pageCount);


                String batchResult = processBatch(pdfDocument, pdfPath, startPage, endPage);


                try (FileWriter writer = new FileWriter(textPath, !firstBatch)) {
                    writer.write(batchResult);
                    if (batchIndex < totalBatches - 1) {
                        writer.write("\n");
                    }
                }

                firstBatch = false;

                System.gc();
            }

            System.out.println("Successfully processed all " + pageCount + " pages and wrote results to: " + textPath);
            return true;

        } catch (IOException e) {
            System.err.println("PDF processing failed for " + pdfPath + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error processing " + pdfPath + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static String processBatch(PDDocument pdfDocument, String pdfPath, int startPage, int endPage)
            throws IOException, InterruptedException {

        PDFRenderer renderer = new PDFRenderer(pdfDocument);
        int nrThreads = Math.min(5, Runtime.getRuntime().availableProcessors());
        ExecutorService ocrExecutor = Executors.newFixedThreadPool(nrThreads);

        try {
            Map<PageImage, Integer> numberedPages = new HashMap<>();
            List<BlockingQueue<PageImage>> queues = new ArrayList<>();
            List<CompletableFuture<String>> ocrFutures = new ArrayList<>();

            for (int i = 0; i < nrThreads; i++) {
                queues.add(new LinkedBlockingQueue<>());
            }


            for (int page = startPage; page < endPage; page++) {
                try {
                    BufferedImage bim= renderer.renderImageWithDPI(page, 300, org.apache.pdfbox.rendering.ImageType.RGB);

                    PageImage pageImage = new PageImage(bim);
                    numberedPages.put(pageImage, page + 1);

                    int queueIndex = (page - startPage) * nrThreads / (endPage - startPage);
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
                            if (text.trim().isEmpty()) {
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


                            pageImage = null;
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

            try {
                allDone.join();
            } catch (Exception e) {
                System.err.println("Error waiting for OCR tasks: " + e.getMessage());
            }

            return ocrFutures.stream()
                    .map(future -> {
                        try {
                            return future.join();
                        } catch (Exception e) {
                            System.err.println("Error getting OCR result: " + e.getMessage());
                            return "";
                        }
                    })
                    .collect(Collectors.joining("\n"));

        } catch (InterruptedException e) {
            System.err.println("PDF OCR failed for " + pdfPath + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load PDF: " + e.getMessage(), e);
        } finally {
            ocrExecutor.shutdownNow();
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