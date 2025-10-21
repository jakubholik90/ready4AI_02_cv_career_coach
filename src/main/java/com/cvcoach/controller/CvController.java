package com.cvcoach.controller;

import com.cvcoach.model.CvData;
import com.cvcoach.model.CvDataEntity;
import com.cvcoach.model.JobPosition;
import com.cvcoach.repository.CvDataRepository;
import com.cvcoach.service.CvAnalysisService;
import com.cvcoach.service.JobSearchService;
import com.cvcoach.service.PdfParserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for CV analysis and job search operations
 */
@RestController
@RequestMapping("/api/cv")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*") // Allow all origins for development
public class CvController {

    private final PdfParserService pdfParserService;
    private final CvAnalysisService cvAnalysisService;
    private final CvDataRepository cvDataRepository;
    private final JobSearchService jobSearchService;

    /**
     * Upload and analyze CV from PDF file
     *
     * @param file PDF file containing CV
     * @return Analyzed CV data
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAndAnalyzeCv(@RequestParam("file") MultipartFile file) {

        log.info("Received CV upload request. File: {}, Size: {} bytes",
                file.getOriginalFilename(), file.getSize());

        try {
            // Validate file
            if (file.isEmpty()) {
                log.warn("Empty file received");
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("File is empty"));
            }

            if (!isValidPdf(file)) {
                log.warn("Invalid file type: {}", file.getContentType());
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body(new ErrorResponse("Only PDF files are supported"));
            }

            // Check file size (5MB limit)
            if (file.getSize() > 5 * 1024 * 1024) {
                log.warn("File too large: {} bytes", file.getSize());
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body(new ErrorResponse("File size exceeds 5MB limit"));
            }

            // Extract text from PDF
            log.info("Extracting text from PDF...");
            String cvText = pdfParserService.extractText(file.getInputStream());

            if (cvText == null || cvText.trim().isEmpty()) {
                log.warn("No text extracted from PDF");
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Could not extract text from PDF. Make sure it's not an image-only PDF."));
            }

            log.info("Extracted {} characters from PDF", cvText.length());

            // Analyze CV using AI
            log.info("Analyzing CV with OpenAI...");
            CvData cvData = cvAnalysisService.analyzeCv(cvText);

            // Save to database
            CvDataEntity entity = CvDataEntity.fromDto(cvData);
            CvDataEntity saved = cvDataRepository.save(entity);

            log.info("CV data saved to database with ID: {}", saved.getId());

            return ResponseEntity.ok(cvData);

        } catch (IOException e) {
            log.error("Error processing PDF file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error processing PDF file: " + e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error during CV analysis", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to analyze CV: " + e.getMessage()));
        }
    }

    /**
     * Get the most recent CV data
     *
     * @return Latest CV data from database
     */
    @GetMapping("/data")
    public ResponseEntity<?> getCvData() {
        log.info("Fetching latest CV data");

        Optional<CvDataEntity> entity = cvDataRepository.findTopByOrderByCreatedAtDesc();

        if (entity.isEmpty()) {
            log.warn("No CV data found in database");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("No CV data found. Please upload a CV first."));
        }

        log.info("Found CV data with ID: {}", entity.get().getId());
        return ResponseEntity.ok(entity.get().toDto());
    }

    /**
     * Find job positions matching the CV profile
     *
     * @return List of matching job positions
     */
    @GetMapping("/jobs/matching")
    public ResponseEntity<?> getMatchingJobs() {
        log.info("Searching for matching jobs");

        Optional<CvDataEntity> cvData = cvDataRepository.findTopByOrderByCreatedAtDesc();

        if (cvData.isEmpty()) {
            log.warn("No CV data available for job search");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("No CV data found. Please upload a CV first."));
        }

        try {
            List<JobPosition> jobs = jobSearchService.findMatchingJobs(cvData.get().toDto());
            log.info("Found {} matching job positions", jobs.size());
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            log.error("Error finding matching jobs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to find matching jobs: " + e.getMessage()));
        }
    }

    /**
     * Find alternative job positions (different field/location)
     *
     * @return List of alternative job positions
     */
    @GetMapping("/jobs/alternative")
    public ResponseEntity<?> getAlternativeJobs() {
        log.info("Searching for alternative jobs");

        Optional<CvDataEntity> cvData = cvDataRepository.findTopByOrderByCreatedAtDesc();

        if (cvData.isEmpty()) {
            log.warn("No CV data available for job search");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("No CV data found. Please upload a CV first."));
        }

        try {
            List<JobPosition> jobs = jobSearchService.findAlternativeJobs(cvData.get().toDto());
            log.info("Found {} alternative job positions", jobs.size());
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            log.error("Error finding alternative jobs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to find alternative jobs: " + e.getMessage()));
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("CV Career Coach API is running");
    }

    /**
     * Validate if file is a PDF
     */
    private boolean isValidPdf(MultipartFile file) {
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();

        return (contentType != null && contentType.equals("application/pdf")) ||
                (filename != null && filename.toLowerCase().endsWith(".pdf"));
    }

    /**
     * Error response DTO
     */
    private record ErrorResponse(String message) {}
}