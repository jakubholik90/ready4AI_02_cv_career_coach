package com.cvcoach.service;

import com.cvcoach.model.CvData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Service for analyzing CV content using OpenAI
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CvAnalysisService {

    private final ChatClient.Builder chatClientBuilder;
    private final ObjectMapper objectMapper;

    /**
     * Analyze CV text and extract structured information using AI
     *
     * @param cvText Raw text extracted from CV
     * @return Structured CV data
     */
    public CvData analyzeCv(String cvText) {
        log.info("Starting CV analysis with OpenAI");

        try {
            String promptText = """
                    Analyze the following CV and extract information in JSON format.
                    
                    Required fields:
                    - location: string (city and country)
                    - hardSkills: string (semicolon-separated list of technical skills)
                    - softSkills: string (semicolon-separated list of soft skills)
                    - education: string (highest degree and field)
                    - totalExperienceYears: integer (total years of work experience)
                    - jobBranch: string (main professional field/industry)
                    - branchExperienceYears: integer (years in the main field)
                    
                    CV Content:
                    {cvContent}
                    
                    Respond ONLY with valid JSON, no additional text or markdown formatting.
                    """;

            PromptTemplate promptTemplate = new PromptTemplate(promptText);
            Prompt prompt = promptTemplate.create(Map.of("cvContent", cvText));

            ChatClient chatClient = chatClientBuilder.build();

            log.debug("Sending request to OpenAI API...");

            String response = chatClient.prompt(prompt)
                    .call()
                    .content();

            log.debug("OpenAI raw response: {}", response);

            return parseAiResponse(response);

        } catch (Exception e) {
            log.error("Failed to analyze CV with OpenAI", e);

            // Check for common issues
            if (e.getMessage().contains("401") || e.getMessage().contains("Unauthorized")) {
                throw new RuntimeException("Invalid OpenAI API key. Please check your OPENAI_API_KEY environment variable.", e);
            }
            if (e.getMessage().contains("429") || e.getMessage().contains("quota")) {
                throw new RuntimeException("OpenAI API quota exceeded. Please check your account at https://platform.openai.com/usage", e);
            }
            if (e.getMessage().contains("timeout")) {
                throw new RuntimeException("OpenAI API timeout. Please try again.", e);
            }

            throw new RuntimeException("Failed to analyze CV: " + e.getMessage(), e);
        }
    }

    /**
     * Analyze CV from file path (backward compatibility)
     *
     * @param cvPath Path to CV PDF file
     * @return Structured CV data
     * @throws IOException if file reading fails
     */
    public CvData analyzeCvFromFile(Path cvPath) throws IOException {
        log.info("Reading CV from file: {}", cvPath);

        // This would require PdfParserService - kept for backward compatibility
        throw new UnsupportedOperationException(
                "Use analyzeCv(String cvText) instead. Extract text first using PdfParserService."
        );
    }

    /**
     * Parse AI response into CvData object
     * Handles both plain JSON and markdown-wrapped JSON
     */
    private CvData parseAiResponse(String response) {
        try {
            // Clean response - remove markdown code blocks if present
            String cleanJson = response.trim();

            if (cleanJson.startsWith("```json")) {
                cleanJson = cleanJson.substring(7);
            } else if (cleanJson.startsWith("```")) {
                cleanJson = cleanJson.substring(3);
            }

            if (cleanJson.endsWith("```")) {
                cleanJson = cleanJson.substring(0, cleanJson.length() - 3);
            }

            cleanJson = cleanJson.trim();

            log.debug("Cleaned JSON: {}", cleanJson);

            // Parse JSON to CvData
            CvData cvData = objectMapper.readValue(cleanJson, CvData.class);

            log.info("Successfully parsed CV data: location={}, branch={}",
                    cvData.getLocation(), cvData.getJobBranch());

            return cvData;

        } catch (JsonProcessingException e) {
            log.error("Failed to parse AI response as JSON: {}", response, e);
            throw new RuntimeException("Failed to parse AI response: " + e.getMessage(), e);
        }
    }
}