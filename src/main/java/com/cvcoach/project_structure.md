cv-career-coach/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── cvcoach/
│       │           ├── CvCareerCoachApplication.java (main entry point)
│       │           ├── model/
│       │           │   ├── CvData.java (CV data model)
│       │           │   └── JobPosition.java (job position model)
│       │           ├── service/
│       │           │   ├── PdfParserService.java (PDF reading)
│       │           │   ├── CvAnalysisService.java (CV analysis with AI)
│       │           │   ├── JobSearchService.java (job search with AI)
│       │           │   └── CsvStorageService.java (CSV storage)
│       │           └── ui/
│       │               └── ConsoleMenu.java (console interface)
│       └── resources/
│           └── application.properties (configuration)
├── cv/
│   └── (place your CV PDF here)
├── data/
│   └── cv_data.csv (generated CV data storage)
└── pom.xml