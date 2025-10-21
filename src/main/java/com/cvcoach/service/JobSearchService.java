package com.cvcoach.service;

import com.cvcoach.model.CvData;
import com.cvcoach.model.JobPosition;
import com.fasterxml.jackson.core.JsonProcessingException;
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

/**
 * Service for finding job positions based on CV data using OpenAI
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JobSearchService {

    private final ChatClient.Builder chatClientBuilder;
    private final ObjectMapper objectMapper;

    /**
     * Find 3 job positions that match the CV profile
     *
     * @param cvData Structured CV information
     * @return List of 3 matching job positions
     */
    public List<JobPosition> findMatchingJobs(CvData cvData) {
        log.info("Searching for matching jobs for: {} in {}", cvData.getJobBranch(), cvData.getLocation());

        String promptText = """
                Based on the following CV profile, suggest 3 job positions that match this person's skills and experience.
                
                CV Profile:
                - Location: {location}
                - Job Branch: {jobBranch}
                - Hard Skills: {hardSkills}
                - Soft Skills: {softSkills}
                - Education: {education}
                - Total Experience: {totalExperience} years
                - Branch Experience: {branchExperience} years
                
                Requirements:
                1. Jobs should match the current location and field
                2. Jobs should align with experience level
                3. Jobs should utilize existing skills
                
                Respond with a JSON array of 3 job positions. Each position must have:
                - position: string (job title)
                - company: string (company name, can be generic like "Tech Company")
                - requirements: string (required skills and experience)
                - matchReason: string (why this job matches the profile)
                
                Respond ONLY with valid JSON array, no additional text or markdown formatting.
                Example format:
                [
                  {
                    "position": "Senior Java Developer",
                    "company": "Tech Solutions GmbH",
                    "requirements": "5+ years Java, Spring Boot, MySQL",
                    "matchReason": "Perfect match for your Java and Spring expertise"
                  }
                ]
                """;

        return searchJobs(cvData, promptText);
    }

    /**
     * Find 3 alternative job positions in different fields or locations
     *
     * @param cvData Structured CV information
     * @return List of 3 alternative job positions
     */
    public List<JobPosition> findAlternativeJobs(CvData cvData) {
        log.info("Searching for alternative career paths for: {}", cvData.getJobBranch());

        String promptText = """
                Based on the following CV profile, suggest 3 ALTERNATIVE career paths in DIFFERENT fields or locations.
                
                CV Profile:
                - Location: {location}
                - Job Branch: {jobBranch}
                - Hard Skills: {hardSkills}
                - Soft Skills: {softSkills}
                - Education: {education}
                - Total Experience: {totalExperience} years
                - Branch Experience: {branchExperience} years
                
                Requirements:
                1. Suggest jobs in DIFFERENT industries or roles
                2. Consider transferable skills
                3. May be in different locations (remote or other cities)
                4. Should still be realistic career transitions
                
                Respond with a JSON array of 3 job positions. Each position must have:
                - position: string (job title)
                - company: string (company name, can be generic)
                - requirements: string (required skills and experience)
                - matchReason: string (why this alternative path makes sense)
                
                Respond ONLY with valid JSON array, no additional text or markdown formatting.
                """;

        return searchJobs(cvData, promptText);
    }

    /**
     * Common method for searching jobs using OpenAI
     */
    private List<JobPosition> searchJobs(CvData cvData, String promptText) {
        PromptTemplate promptTemplate = new PromptTemplate(promptText);

        Prompt prompt = promptTemplate.create(Map.of(
                "location", cvData.getLocation(),
                "jobBranch", cvData.getJobBranch(),
                "hardSkills", cvData.getHardSkills(),
                "softSkills", cvData.getSoftSkills(),
                "education", cvData.getEducation(),
                "totalExperience", cvData.getTotalExperienceYears().toString(),
                "branchExperience", cvData.getBranchExperienceYears().toString()
        ));

        ChatClient chatClient = chatClientBuilder.build();

        String response = chatClient.prompt(prompt)
                .call()
                .content();

        log.debug("OpenAI raw response: {}", response);

        return parseJobsResponse(response);
    }

    /**
     * Parse AI response into list of JobPosition objects
     * Handles both plain JSON and markdown-wrapped JSON
     */
    private List<JobPosition> parseJobsResponse(String response) {
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

            // Parse JSON array to List<JobPosition>
            List<JobPosition> jobs = objectMapper.readValue(
                    cleanJson,
                    new TypeReference<List<JobPosition>>() {}
            );

            log.info("Successfully parsed {} job positions", jobs.size());

            return jobs;

        } catch (JsonProcessingException e) {
            log.error("Failed to parse AI response as JSON: {}", response, e);
            throw new RuntimeException("Failed to parse job positions: " + e.getMessage(), e);
        }
    }
}