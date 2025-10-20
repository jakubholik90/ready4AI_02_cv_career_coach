package com.cvcoach;

import com.cvcoach.model.CvData;
import com.cvcoach.model.JobPosition;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LombokTest {

    @Test
    public void testCvDataLombokAnnotations() {
        // Test @Builder
        CvData cvData = CvData.builder()
                .currentLocation("Warsaw, Poland")
                .education("Master's in Computer Science")
                .totalYearsExperience(5)
                .build();

        // Test @Data generated getters
        assertEquals("Warsaw, Poland", cvData.getCurrentLocation());
        assertEquals("Master's in Computer Science", cvData.getEducation());
        assertEquals(5, cvData.getTotalYearsExperience());

        // Test @Data generated setters
        cvData.setCurrentLocation("Krakow, Poland");
        assertEquals("Krakow, Poland", cvData.getCurrentLocation());

        // Test @Data generated equals and hashCode
        CvData cvData2 = CvData.builder()
                .currentLocation("Krakow, Poland")
                .education("Master's in Computer Science")
                .totalYearsExperience(5)
                .build();

        assertEquals(cvData, cvData2);
        assertEquals(cvData.hashCode(), cvData2.hashCode());

        // Test @NoArgsConstructor
        CvData emptyCvData = new CvData();
        assertNotNull(emptyCvData);

        // Test @AllArgsConstructor
        CvData fullCvData = new CvData("Berlin, Germany", null, null, "PhD", 10, "IT", 8);
        assertEquals("Berlin, Germany", fullCvData.getCurrentLocation());
        assertEquals("PhD", fullCvData.getEducation());
        assertEquals(10, fullCvData.getTotalYearsExperience());
    }

    @Test
    public void testJobPositionLombokAnnotations() {
        // Test @Builder
        JobPosition job = JobPosition.builder()
                .title("Software Engineer")
                .company("Tech Corp")
                .location("Remote")
                .build();

        // Test @Data generated getters
        assertEquals("Software Engineer", job.getTitle());
        assertEquals("Tech Corp", job.getCompany());
        assertEquals("Remote", job.getLocation());

        // Test @Data generated setters
        job.setTitle("Senior Software Engineer");
        assertEquals("Senior Software Engineer", job.getTitle());

        // Test @Data generated equals and hashCode
        JobPosition job2 = JobPosition.builder()
                .title("Senior Software Engineer")
                .company("Tech Corp")
                .location("Remote")
                .build();

        assertEquals(job, job2);
        assertEquals(job.hashCode(), job2.hashCode());

        // Test @NoArgsConstructor
        JobPosition emptyJob = new JobPosition();
        assertNotNull(emptyJob);

        // Test @AllArgsConstructor
        JobPosition fullJob = new JobPosition("DevOps Engineer", "Cloud Inc", "NYC", "AWS, Docker", "Senior", "Cloud infrastructure", "Great match");
        assertEquals("DevOps Engineer", fullJob.getTitle());
        assertEquals("Cloud Inc", fullJob.getCompany());
        assertEquals("NYC", fullJob.getLocation());
    }
}
