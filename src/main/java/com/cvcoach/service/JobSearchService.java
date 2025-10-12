package com.cvcoach.service;

import com.cvcoach.model.CvData;
import com.cvcoach.model.JobPosition;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobSearchService {

    private final ChatClient.Builder chatClientBuilder;
    private final ObjectMapper objectMapper;

    private static final String JOB_SEARCH_PROMPT = """
        Based on the following candidate profile, suggest 3 realistic job positions that would be a good match.
        Return ONLY a valid JSON array with the following structure (no markdown, no extra text):
        [
          {
            "title": "Job Title",
            "company": "Company Name (can be generic like 'Tech Companies' or 'Financial Institutions')",
            "location": "City, Country",
            "required_skills": "Main skills required",
            "experience_level": "Junior/Mid/Senior",
            "description": "Brief job description",
            "match_reason": "Why this matches the candidate"
          }
        ]
        
        Candidate Profile:
        - Location: {location}
        - Hard Skills: {hard_skills}
        - Soft Skills: {soft_skills}
        - Education: {education}
        - Total Experience: {total_experience} years
        - Current Job Branch: {job_branch}
        - Years in Branch: {branch_experience} years
        
        {ignore_instruction}
        
        Return only the JSON array with exactly 3 job positions, nothing else.
        """;

    /**
     * Searches for job positions matching CV data
     */
    public List<JobPosition> searchJobs(CvData cvData, boolean ignoreSpecificData) throws Exception {
        log.info("Starting job search... (ignore specific data: {})", ignoreSpecificData);

        String ignoreInstruction = ignoreSpecificData
                ? "IMPORTANT: Suggest jobs in DIFFERENT locations and fields than the candidate's current profile. Be creative and suggest alternative career paths."
                : "Suggest jobs that closely match the candidate's current profile and location.";

        // Create prompt
        PromptTemplate promptTemplate = new PromptTemplate(JOB_SEARCH_PROMPT);
        Prompt prompt = promptTemplate.create(Map.of(
                "location", cvData.getCurrentLocation() != null ? cvData.getCurrentLocation() : "Not specified",
                "hard_skills", cvData.getHardSkills() != null ? String.join(", ", cvData.getHardSkills()) : "Not specified",
                "soft_skills", cvData.getSoftSkills() != null ? String.join(", ", cvData.getSoftSkills()) : "Not specified",
                "education", cvData.getEducation() != null ? cvData.getEducation() : "Not specified",
                "total_experience", cvData.getTotalYearsExperience() != null ? cvData.getTotalYearsExperience().toString() : "0",
                "job_branch", cvData.getCurrentJobBranch() != null ? cvData.getCurrentJobBranch() : "Not specified",
                "branch_experience", cvData.getYearsInCurrentBranch() != null ? cvData.getYearsInCurrentBranch().toString() : "0",
                "ignore_instruction", ignoreInstruction
        ));

        // Call OpenAI API
        ChatClient chatClient = chatClientBuilder.build();
        String response = chatClient.prompt(prompt).call().content();

        log.info("Received AI response for job search");

        // Parse JSON response
        List<JobPosition> jobs = parseJobsResponse(response);

        log.info("Found {} job positions", jobs.size());
        return jobs;
    }

    /**
     * Parses JSON array response from AI
     */
    private List<JobPosition> parseJobsResponse(String response) throws Exception {
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
            return objectMapper.readValue(cleanJson, new TypeReference<List<JobPosition>>() {});
        } catch (Exception e) {
            log.error("Failed to parse JSON response: {}", cleanJson);
            throw new Exception("Failed to parse AI response. Please try again.", e);
        }
    }
}