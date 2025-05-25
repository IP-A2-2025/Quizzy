package com.backend;

import com.backend.utils.PDFtoText;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class BackendApplication {
    public static void main(String[] args) {

        SpringApplication.run(BackendApplication.class, args);
        //PDFtoText pdftotext = new PDFtoText();
        //pdftotext.generateTextFromPDF();
    }
}