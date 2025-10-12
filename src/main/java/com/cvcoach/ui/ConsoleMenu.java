package com.cvcoach.ui;

import com.cvcoach.model.CvData;
import com.cvcoach.model.JobPosition;
import com.cvcoach.service.CvAnalysisService;
import com.cvcoach.service.CsvStorageService;
import com.cvcoach.service.JobSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsoleMenu implements CommandLineRunner {

    private final CvAnalysisService cvAnalysisService;
    private final JobSearchService jobSearchService;
    private final CsvStorageService csvStorageService;

    private CvData currentCvData;
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) {
        printWelcome();

        boolean running = true;
        while (running) {
            printMenu();

            try {
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1" -> handleReadCv();
                    case "2" -> handleSearchJobs(false);
                    case "3" -> handleSearchJobs(true);
                    case "4" -> {
                        System.out.println("\nThank you for using CV Career Coach. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("\nInvalid choice. Please select 1-4.");
                }

            } catch (Exception e) {
                System.err.println("\nError: " + e.getMessage());
                log.error("Error in menu operation", e);
            }
        }

        scanner.close();
    }

    private void printWelcome() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("   CV CAREER COACH");
        System.out.println("   Powered by AI");
        System.out.println("=".repeat(50));
    }

    private void printMenu() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("MAIN MENU");
        System.out.println("-".repeat(50));
        System.out.println("1 - Read and show skills from CV");
        System.out.println("2 - Search 3 job positions matching your profile");
        System.out.println("3 - Search 3 alternative job positions");
        System.out.println("4 - Exit");
        System.out.println("-".repeat(50));
        System.out.print("Enter your choice (1-4): ");
    }

    private void handleReadCv() {
        System.out.println("\n[Processing CV...]");

        try {
            currentCvData = cvAnalysisService.analyzeCv();
            System.out.println(currentCvData);
            System.out.println("✓ CV data saved to data/cv_data.csv");

        } catch (Exception e) {
            System.err.println("\n✗ Failed to analyze CV: " + e.getMessage());
            System.err.println("  Make sure:");
            System.err.println("  1. You have placed your CV PDF in the 'cv' folder");
            System.err.println("  2. Your OpenAI API key is configured in application.properties");
        }
    }

    private void handleSearchJobs(boolean ignoreSpecificData) {
        if (currentCvData == null) {
            System.out.println("\n⚠ No CV data available.");
            System.out.println("  Please analyze your CV first (Option 1).");
            return;
        }

        String searchType = ignoreSpecificData ? "alternative" : "matching";
        System.out.println("\n[Searching for " + searchType + " job positions...]");

        try {
            List<JobPosition> jobs = jobSearchService.searchJobs(currentCvData, ignoreSpecificData);

            System.out.println("\n" + "=".repeat(50));
            System.out.println("  FOUND " + jobs.size() + " JOB POSITIONS");
            System.out.println("=".repeat(50));

            for (int i = 0; i < jobs.size(); i++) {
                System.out.println("\n[" + (i + 1) + "] " + jobs.get(i));
            }

        } catch (Exception e) {
            System.err.println("\n✗ Failed to search jobs: " + e.getMessage());
            System.err.println("  Make sure your OpenAI API key is configured correctly.");
        }
    }
}
