package com.cvcoach.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CvData {

    @JsonProperty("current_location")
    private String currentLocation;

    @JsonProperty("hard_skills")
    private List<String> hardSkills;

    @JsonProperty("soft_skills")
    private List<String> softSkills;

    @JsonProperty("education")
    private String education;

    @JsonProperty("total_years_experience")
    private Integer totalYearsExperience;

    @JsonProperty("current_job_branch")
    private String currentJobBranch;

    @JsonProperty("years_in_current_branch")
    private Integer yearsInCurrentBranch;

    /**
     * Converts CV data to CSV row format
     */
    public String[] toCsvRow() {
        return new String[]{
                currentLocation != null ? currentLocation : "",
                hardSkills != null ? String.join(";", hardSkills) : "",
                softSkills != null ? String.join(";", softSkills) : "",
                education != null ? education : "",
                totalYearsExperience != null ? totalYearsExperience.toString() : "0",
                currentJobBranch != null ? currentJobBranch : "",
                yearsInCurrentBranch != null ? yearsInCurrentBranch.toString() : "0"
        };
    }

    /**
     * Returns CSV header
     */
    public static String[] getCsvHeader() {
        return new String[]{
                "Location",
                "Hard Skills",
                "Soft Skills",
                "Education",
                "Total Experience (Years)",
                "Job Branch",
                "Branch Experience (Years)"
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== CV Analysis Results ===\n");
        sb.append("Location: ").append(currentLocation).append("\n");
        sb.append("Hard Skills: ").append(hardSkills).append("\n");
        sb.append("Soft Skills: ").append(softSkills).append("\n");
        sb.append("Education: ").append(education).append("\n");
        sb.append("Total Experience: ").append(totalYearsExperience).append(" years\n");
        sb.append("Current Job Branch: ").append(currentJobBranch).append("\n");
        sb.append("Years in Branch: ").append(yearsInCurrentBranch).append(" years\n");
        sb.append("===========================\n");
        return sb.toString();
    }
}