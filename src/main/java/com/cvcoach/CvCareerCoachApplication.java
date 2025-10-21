package com.cvcoach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CV Career Coach - AI-powered CV analysis and job matching application
 *
 * Features:
 * - PDF CV upload and analysis
 * - OpenAI-powered CV parsing
 * - Job position matching
 * - Alternative career path suggestions
 *
 * @author Your Name
 * @version 1.0.0
 */
@SpringBootApplication
public class CvCareerCoachApplication {

    public static void main(String[] args) {
        SpringApplication.run(CvCareerCoachApplication.class, args);
        System.out.println("""
            
            ========================================
            CV Career Coach - Started Successfully!
            ========================================
            
            Access the application at:
            http://localhost:8080
            
            API Endpoints:
            - POST /api/cv/upload (Upload PDF)
            - GET  /api/cv/data (Get CV data)
            - GET  /api/cv/jobs/matching (Matching jobs)
            - GET  /api/cv/jobs/alternative (Alternative jobs)
            - GET  /api/cv/health (Health check)
            
            ========================================
            """);
    }
}