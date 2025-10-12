package com.cvcoach.service;

import com.cvcoach.model.CvData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CvAnalysisService {

    private final ChatClient.Builder chatClientBuilder;
    private final ObjectMapper objectMapper;
    private final PdfParserService pdfParserService;
    private final CsvStorageService csvStorageService;

    private static final String CV_ANALYSIS_PROMPT = """
        Analyze the following CV and extract structured information.
        Return ONLY a valid JSON object with the following structure (no markdown, no extra text):
        {
          "current_location": "city, country",
          "hard_skills": ["skill1", "skill2", "skill3"],
          "soft_skills": ["language1", "language2"],
          "education": "highest degree and field",
          "total_years_experience": number,
          "current_job_branch": "industry/field name",
          "years_in_current_branch": number
        }
        
        CV Content:
        {cv_text}
        
        Return only the JSON object, nothing else.
        """;

    /**
     * Analyzes CV and extracts structured data using OpenAI
     */
    public CvData analyzeCv() throws Exception {
        log.info("Starting CV analysis...");

        // Extract text from PDF
        String cvText = pdfParserService.extractTextFromCv();

        // Create prompt
        PromptTemplate promptTemplate = new PromptTemplate(CV_ANALYSIS_PROMPT);
        Prompt prompt = promptTemplate.create(Map.of("cv_text", cvText));

        // Call OpenAI API
        ChatClient chatClient = chatClientBuilder.build();
        String response = chatClient.prompt(prompt).call().content();

        log.info("Received AI response: {}", response);

        // Parse JSON response
        CvData cvData = parseJsonResponse(response);

        // Save to CSV
        csvStorageService.saveCvData(cvData);

        log.info("CV analysis completed successfully");
        return cvData;
    }

    /**
     * Parses JSON response from AI, handling potential markdown wrapping
     */
    private CvData parseJsonResponse(String response) throws Exception {
        // Clean up response - remove markdown code blocks if present
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

        try {
            return objectMapper.readValue(cleanJson, CvData.class);
        } catch (Exception e) {
            log.error("Failed to parse JSON response: {}", cleanJson);
            throw new Exception("Failed to parse AI response. Please try again.", e);
        }
    }
}