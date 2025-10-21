package com.cvcoach.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for job position information
 * Used for JSON serialization/deserialization with OpenAI API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPosition {

    @JsonProperty("position")
    private String position;

    @JsonProperty("company")
    private String company;

    @JsonProperty("requirements")
    private String requirements;

    @JsonProperty("matchReason")
    private String matchReason;

    @Override
    public String toString() {
        return String.format(
                "JobPosition{position='%s', company='%s'}",
                position, company
        );
    }
}