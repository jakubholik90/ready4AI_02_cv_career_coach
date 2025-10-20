package com.cvcoach.service;

import com.cvcoach.model.CvData;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Service
public class CsvStorageService {

    private static final String DATA_FOLDER = "data";
    private static final String CSV_FILE = DATA_FOLDER + "/cv_data.csv";

    /**
     * Saves CV data to CSV file
     */
    public void saveCvData(CvData cvData) throws IOException {
        ensureDataFolderExists();

        try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE))) {
            // Write header
            writer.writeNext(CvData.getCsvHeader());

            // Write data
            writer.writeNext(cvData.toCsvRow());

            log.info("CV data saved to {}", CSV_FILE);
        }
    }

    /**
     * Checks if CV data file exists
     */
    public boolean cvDataExists() {
        return new File(CSV_FILE).exists();
    }

    /**
     * Ensures data folder exists
     */
    private void ensureDataFolderExists() throws IOException {
        File dataFolder = new File(DATA_FOLDER);
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdirs()) {
                throw new IOException("Failed to create data folder");
            }
            log.info("Created data folder: {}", DATA_FOLDER);
        }
    }

    /**
     * Reads existing CV data (if needed for future features)
     */
    public String readCsvContent() throws IOException {
        if (!cvDataExists()) {
            return "No CV data found. Please analyze your CV first (Option 1).";
        }
        return new String(Files.readAllBytes(Paths.get(CSV_FILE)));
    }
}