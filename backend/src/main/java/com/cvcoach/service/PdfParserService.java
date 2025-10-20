package com.cvcoach.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class PdfParserService {

    private static final String CV_FOLDER = "cv";

    /**
     * Reads and extracts text from the first PDF file found in cv/ folder
     */
    public String extractTextFromCv() throws IOException {
        File cvFolder = new File(CV_FOLDER);

        if (!cvFolder.exists() || !cvFolder.isDirectory()) {
            throw new IOException("CV folder not found. Please create 'cv' folder and place your CV PDF there.");
        }

        File[] pdfFiles = cvFolder.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".pdf"));

        if (pdfFiles == null || pdfFiles.length == 0) {
            throw new IOException("No PDF file found in cv/ folder. Please place your CV PDF there.");
        }

        File cvFile = pdfFiles[0];
        log.info("Reading CV from: {}", cvFile.getName());

        return extractTextFromPdf(cvFile);
    }

    /**
     * Extracts text content from a PDF file
     */
    private String extractTextFromPdf(File pdfFile) throws IOException {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            if (text == null || text.trim().isEmpty()) {
                throw new IOException("PDF file appears to be empty or unreadable");
            }

            log.info("Successfully extracted {} characters from PDF", text.length());
            return text;
        }
    }
}
