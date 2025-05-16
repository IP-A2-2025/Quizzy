package com.backend;

import com.backend.utils.PDFtoText;
import org.glassfish.jaxb.runtime.v2.runtime.property.StructureLoaderBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

public class PDFtoTextTest {
    @TempDir
    private Path tempDir;

    private final String successPdfPath = System.getProperty("user.dir")
            + File.separator + "test_courses"
            + File.separator + "success.pdf";

    private final String blankPdfPath = System.getProperty("user.dir")
            + File.separator + "test_courses"
            + File.separator + "blank_page.pdf";

    @BeforeAll
    static void testExistsNeededDirectories_and_Pdfs() throws IOException {
        Path testCoursesDirectoryPath = Paths.get(System.getProperty("user.dir") + File.separator + "test_courses");
        Path testTessdataDirectoryPath = Paths.get(System.getProperty("user.dir") + File.separator + "tessdata");
        assertTrue(Files.exists(testCoursesDirectoryPath) &&
                Files.isDirectory(testCoursesDirectoryPath) &&
                Files.isReadable(testCoursesDirectoryPath) &&
                Files.isWritable(testCoursesDirectoryPath) &&

                Files.exists(testCoursesDirectoryPath) &&
                Files.isDirectory(testCoursesDirectoryPath) &&
                Files.isReadable(testTessdataDirectoryPath)
        );

        assertTrue(Files.list(testCoursesDirectoryPath)
                .anyMatch(p -> p.toString().endsWith(".pdf")));
    }

    @Test
    void pdfToImage_success() throws IOException {
        Path outputTxtPath = tempDir.resolve("result.txt");

        PDFtoText.pdfToImage(successPdfPath, outputTxtPath.toString());
        String resultString = Files.readString(outputTxtPath);

        assertTrue(resultString.contains("test"));
    }

    @Test
    void pdfToImage_wrong_path() {
        assertThrows(RuntimeException.class, () -> PDFtoText.pdfToImage("", ""));
    }

    @Test
    void pdfToImage_blank_page() throws IOException {
        Path outputTxtPath = tempDir.resolve("result.txt");

        PDFtoText.pdfToImage(blankPdfPath, outputTxtPath.toString());
        String resultString = Files.readString(outputTxtPath);

        assertEquals("", resultString.trim());
    }

    @Test
    void imageToText_success() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String text = "Hello OCR";
        int width = 200;
        int height = 100;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();

        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0, 0, width, height);

        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Arial", Font.PLAIN, 20));

        graphics.drawString(text, 20, 50);
        graphics.dispose();

        var classObj = PDFtoText.class;
        Method imageToText = classObj.getDeclaredMethod("imageToText", BufferedImage.class);
        imageToText.setAccessible(true);

        String result = (String) imageToText.invoke(null, bufferedImage);

        assertEquals(text, result.trim());
    }

    @Test
    void imageToText_blank_image() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int width = 200;
        int height = 100;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();

        graphics.setBackground(Color.WHITE);
        graphics.dispose();

        var classObj = PDFtoText.class;
        Method imageToText = classObj.getDeclaredMethod("imageToText", BufferedImage.class);
        imageToText.setAccessible(true);

        String result = (String) imageToText.invoke(null, bufferedImage);

        assertEquals("", result.trim());
    }
}