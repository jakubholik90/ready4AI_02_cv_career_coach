package com.cvcoach.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * JPA Entity for storing CV data in MySQL database
 */
@Entity
@Table(name = "cv_data")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CvDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String location;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String hardSkills;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String softSkills;

    @Column(nullable = false, length = 500)
    private String education;

    @Column(nullable = false)
    private Integer totalExperienceYears;

    @Column(nullable = false, length = 200)
    private String jobBranch;

    @Column(nullable = false)
    private Integer branchExperienceYears;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Convert entity to DTO
     */
    public CvData toDto() {
        CvData dto = new CvData();
        dto.setLocation(this.location);
        dto.setHardSkills(this.hardSkills);
        dto.setSoftSkills(this.softSkills);
        dto.setEducation(this.education);
        dto.setTotalExperienceYears(this.totalExperienceYears);
        dto.setJobBranch(this.jobBranch);
        dto.setBranchExperienceYears(this.branchExperienceYears);
        return dto;
    }

    /**
     * Create entity from DTO
     */
    public static CvDataEntity fromDto(CvData dto) {
        return CvDataEntity.builder()
                .location(dto.getLocation())
                .hardSkills(dto.getHardSkills())
                .softSkills(dto.getSoftSkills())
                .education(dto.getEducation())
                .totalExperienceYears(dto.getTotalExperienceYears())
                .jobBranch(dto.getJobBranch())
                .branchExperienceYears(dto.getBranchExperienceYears())
                .build();
    }
}