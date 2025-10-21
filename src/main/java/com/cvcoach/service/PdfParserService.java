package com.cvcoach.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * Service for parsing PDF files and extracting text content
 * Images are ignored - only text is extracted
 */
@Service
@Slf4j
public class PdfParserService {

    /**
     * Extract text from PDF InputStream
     *
     * @param inputStream PDF file input stream
     * @return Extracted text content
     * @throws IOException if PDF parsing fails
     */
    public String extractText(InputStream inputStream) throws IOException {
        log.info("Starting PDF text extraction");

        try (PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {

            // Handle encrypted PDFs
            if (document.isEncrypted()) {
                log.warn("PDF is encrypted, attempting to decrypt");
                try {
                    document.setAllSecurityToBeRemoved(true);
                } catch (Exception e) {
                    log.error("Failed to decrypt PDF", e);
                    throw new IOException("PDF is password-protected and cannot be decrypted");
                }
            }

            // Extract text
            PDFTextStripper stripper = new PDFTextStripper();

            // Configure text extraction
            stripper.setSortByPosition(true);
            stripper.setStartPage(1);
            stripper.setEndPage(document.getNumberOfPages());

            String text = stripper.getText(document);

            // Clean up text
            text = cleanText(text);

            log.info("Successfully extracted {} characters from PDF ({} pages)",
                    text.length(), document.getNumberOfPages());

            return text;

        } catch (IOException e) {
            log.error("Error parsing PDF file", e);
            throw new IOException("Failed to parse PDF: " + e.getMessage(), e);
        }
    }

    /**
     * Extract text from CV PDF file (alias for backward compatibility)
     *
     * @param inputStream PDF file input stream
     * @return Extracted text content
     * @throws IOException if PDF parsing fails
     */
    public String extractTextFromCV(InputStream inputStream) throws IOException {
        return extractText(inputStream);
    }

    /**
     * Clean extracted text - remove excessive whitespace and special characters
     */
    private String cleanText(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        return text
                .replaceAll("\\r\\n", "\n")     // Normalize line endings
                .replaceAll("\\r", "\n")        // Normalize line endings
                .replaceAll("\\n{3,}", "\n\n")  // Max 2 consecutive newlines
                .replaceAll("[ \\t]{2,}", " ")  // Max 1 consecutive space
                .trim();
    }
}