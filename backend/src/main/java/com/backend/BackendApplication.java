package com.backend;

import com.backend.flashCardGenerator.PDFtoText;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {
    public static void main(String[] args) {

        SpringApplication.run(BackendApplication.class, args);
        //PDFtoText pdfText = new PDFtoText();
        String courseName = "graph";
        String courseNumber = "test";
        String text = PDFtoText.generateTextFromPDF(courseName, courseNumber);
        System.out.println("Extracted text: " + text);

    }
}
