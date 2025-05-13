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

import lombok.Getter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.tesseract.TessBaseAPI;
import org.bytedeco.leptonica.global.leptonica;

public class PDFtoText {

    public static void generateTextFromPDF() {
        try {
            String coursesPath = System.getProperty("user.dir") + File.separator + "courses";
            List<Path> pdfPaths = Files.walk(Paths.get(coursesPath))
                    .filter(p -> p.toString().endsWith(".pdf"))
                    .toList();

            for (Path p : pdfPaths) {
                String pdfPath = p.toAbsolutePath().toString();
                String textPath = pdfPath.replace(".pdf", ".txt");

                PDFtoText.pdfToImage(pdfPath, textPath);
            }
            System.out.println("All pdfs have been parsed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String imageToText(BufferedImage image) {
        try (TessBaseAPI api = new TessBaseAPI()) {
           /* String tessDataPath = System.getProperty("user.dir") + File.separator + "tessdata";
            if (api.Init(tessDataPath, "ENG") != 0) {
                System.err.println("Could not initialize tesseract.");
                return null;
            }*/

            if (api.Init("tessdata", "ENG") != 0) {
                System.err.println("Could not initialize tesseract.");
                return null;
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
            String stringText = outText != null ? outText.getString() : "The page is blank or ocr failed";

            api.End();
            outText.deallocate();
            leptonica.pixDestroy(pix);

            return stringText != null ? stringText : "The page is blank or ocr failed";

        } catch (Exception e) {
            System.err.println("Error during OCR processing: " + e.getMessage());
        }
        return null;
    }

    public static void pdfToImage(String pdfPath, String textPath) {
        ExecutorService ocrExecutor = null;
        try (PDDocument pdfDocument = Loader.loadPDF(new File(pdfPath))) {
            PDFRenderer renderer = new PDFRenderer(pdfDocument);
            int pageCount = pdfDocument.getNumberOfPages();

            if (pageCount == 0) {
                System.out.println("Skipped " + pdfPath + " because it has no pages.");
                return;
            }

            int nrThreads = 5;
            ocrExecutor = Executors.newFixedThreadPool(nrThreads);

            Map<PageImage, Integer> numberedPages = new HashMap<>();
            List<BlockingQueue<PageImage>> queues = new ArrayList<>();
            List<CompletableFuture<String>> ocrFutures = new ArrayList<>();

            for (int i = 0; i < nrThreads; i++) {
                queues.add(new LinkedBlockingQueue<>());
            }

            for (int page = 0; page < pageCount; page++) {
                try {
                    BufferedImage bim = renderer.renderImageWithDPI(page, 300, org.apache.pdfbox.rendering.ImageType.RGB);
                    PageImage pageImage = new PageImage(bim);
                    numberedPages.put(pageImage, page + 1);

                    int queueIndex = page * nrThreads / pageCount;
                    queues.get(queueIndex).put(pageImage);
                } catch (IOException e) {
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
                                System.out.println("course where failure occured: " + pdfPath);
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

            try (FileWriter writer = new FileWriter(textPath)) {
                writer.write(pdfText);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(textPath);

        } catch (Exception e) {
            System.err.println("PDF OCR failed: " + e.getMessage());
            throw new RuntimeException("OCR failed", e);
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