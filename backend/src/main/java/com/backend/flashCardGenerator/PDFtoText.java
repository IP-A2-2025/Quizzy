package com.backend.flashCardGenerator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.tesseract.TessBaseAPI;
import static org.bytedeco.leptonica.global.leptonica.pixDestroy;

public class PDFtoText {

    public static String generateTextFromPDF(String courseName, String courseNumber) {

        String pdfPath = System.getProperty("user.dir") + File.separator + "courses" + File.separator + courseName + File.separator + courseNumber + ".pdf";
        return pdfToImage(pdfPath);
    }

    private static String pdfToImage(String pdfPath) {
        try (PDDocument pdfDocument = Loader.loadPDF(new File(pdfPath))) {
            StringBuilder pdfText = new StringBuilder();

            PDFRenderer renderer = new PDFRenderer(pdfDocument);
            for (int page = 0; page < pdfDocument.getNumberOfPages(); ++page) {
                BufferedImage bim = renderer.renderImageWithDPI(page, 300, ImageType.RGB);
                pdfText.append(imageToText(bim)).append("\n");
            }
            System.out.println("Extracted text from PDF: " + pdfText.toString());
            return pdfText.toString();
            //FileWriter fileWriter = new FileWriter(pdfPath + ".txt");
            //fileWriter.write(pdfText.toString());
            //fileWriter.close();

        } catch (IOException e) {
            System.err.println("Error processing PDF file: " + e.getMessage());
        }
        return null;
    }

    private static String imageToText(BufferedImage image) {
        try(TessBaseAPI api = new TessBaseAPI();){
            // Convert BufferedImage to Leptonica PIX
            PIX pix = org.bytedeco.leptonica.global.leptonica.pixCreate(image.getWidth(), image.getHeight(), 32);
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int rgb = image.getRGB(x, y);
                    org.bytedeco.leptonica.global.leptonica.pixSetPixel(pix, x, y, rgb);
                }
            }

            BytePointer outText;

            // Initialize tesseract-ocr with English, without specifying tessdata path
            if (api.Init("tessdata", "ENG") != 0) {
                System.err.println("Could not initialize tesseract.");
                System.exit(1);
            }
            // Set the image for OCR
            api.SetImage(pix);

            // Get OCR result
            outText = api.GetUTF8Text();
            String stringText = outText.getString();

            // Destroy used object and release memory
            api.End();
            outText.deallocate();
            pixDestroy(pix);

            return stringText;

        } catch (Exception e) {
            System.err.println("Error during OCR processing: " + e.getMessage());
        }
        return null;
    }

}


