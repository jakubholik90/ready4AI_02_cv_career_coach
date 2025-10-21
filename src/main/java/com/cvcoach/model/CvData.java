package com.cvcoach.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for CV information
 * Used for JSON serialization/deserialization with OpenAI API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CvData {

    @JsonProperty("location")
    private String location;

    @JsonProperty("hardSkills")
    private String hardSkills;

    @JsonProperty("softSkills")
    private String softSkills;

    @JsonProperty("education")
    private String education;

    @JsonProperty("totalExperienceYears")
    private Integer totalExperienceYears;

    @JsonProperty("jobBranch")
    private String jobBranch;

    @JsonProperty("branchExperienceYears")
    private Integer branchExperienceYears;

    @Override
    public String toString() {
        return String.format(
                "CvData{location='%s', jobBranch='%s', experience=%d years, hardSkills='%s'}",
                location, jobBranch, totalExperienceYears, hardSkills
        );
    }
}