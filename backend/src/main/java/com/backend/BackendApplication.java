package com.backend;

import com.backend.utils.PDFtoText;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class BackendApplication {
    public static void main(String[] args) {

        SpringApplication.run(BackendApplication.class, args);
        //PDFtoText pdftotext = new PDFtoText();
        //pdftotext.generateTextFromPDF();
    }
}