package com.cvcoach.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPosition {

    @JsonProperty("title")
    private String title;

    @JsonProperty("company")
    private String company;

    @JsonProperty("location")
    private String location;

    @JsonProperty("required_skills")
    private String requiredSkills;

    @JsonProperty("experience_level")
    private String experienceLevel;

    @JsonProperty("description")
    private String description;

    @JsonProperty("match_reason")
    private String matchReason;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Job Position ---\n");
        sb.append("Title: ").append(title).append("\n");
        sb.append("Company: ").append(company).append("\n");
        sb.append("Location: ").append(location).append("\n");
        sb.append("Required Skills: ").append(requiredSkills).append("\n");
        sb.append("Experience Level: ").append(experienceLevel).append("\n");
        sb.append("Description: ").append(description).append("\n");
        sb.append("Match Reason: ").append(matchReason).append("\n");
        sb.append("-------------------\n");
        return sb.toString();
    }
}