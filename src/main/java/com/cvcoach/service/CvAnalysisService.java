package com.cvcoach.service;

import com.cvcoach.model.CvData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CvAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(CvAnalysisService.class);

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

    public CvAnalysisService(ChatClient.Builder chatClientBuilder,
                             ObjectMapper objectMapper,
                             PdfParserService pdfParserService,
                             CsvStorageService csvStorageService) {
        this.chatClientBuilder = chatClientBuilder;
        this.objectMapper = objectMapper;
        this.pdfParserService = pdfParserService;
        this.csvStorageService = csvStorageService;
    }

    public CvData analyzeCv() throws Exception {
        log.info("Starting CV analysis...");

        String cvText = pdfParserService.extractTextFromCv();

        PromptTemplate promptTemplate = new PromptTemplate(CV_ANALYSIS_PROMPT);
        Prompt prompt = promptTemplate.create(Map.of("cv_text", cvText));

        ChatClient chatClient = chatClientBuilder.build();
        String response = chatClient.prompt(prompt).call().content();

        log.info("Received AI response: {}", response);

        CvData cvData = parseJsonResponse(response);

        csvStorageService.saveCvData(cvData);

        log.info("CV analysis completed successfully");
        return cvData;
    }

    private CvData parseJsonResponse(String response) throws Exception {
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
