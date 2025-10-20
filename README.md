# CV Career Coach

A Java console application that uses OpenAI to analyze your CV and find matching job positions.

## Features

1. **CV Analysis** - Extracts and structures key information from your CV (PDF format)
2. **Job Matching** - Finds 3 job positions that match your profile
3. **Alternative Jobs** - Suggests 3 alternative career paths in different fields/locations

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- OpenAI API key ([Get one here](https://platform.openai.com/api-keys))

## Project Structure

```
cv-career-coach/
├── src/main/java/com/cvcoach/
│   ├── CvCareerCoachApplication.java
│   ├── model/
│   │   ├── CvData.java
│   │   └── JobPosition.java
│   ├── service/
│   │   ├── CvAnalysisService.java
│   │   ├── CsvStorageService.java
│   │   ├── JobSearchService.java
│   │   └── PdfParserService.java
│   └── ui/
│       └── ConsoleMenu.java
├── src/main/resources/
│   └── application.properties
├── cv/                          (create this folder)
│   └── your-cv.pdf             (place your CV here)
├── data/                        (auto-created)
│   └── cv_data.csv             (generated data)
└── pom.xml
```

## Setup Instructions

### 1. Clone/Create Project Structure

Create the project folders:
```bash
mkdir -p cv-career-coach/cv
mkdir -p cv-career-coach/src/main/java/com/cvcoach/{model,service,ui}
mkdir -p cv-career-coach/src/main/resources
cd cv-career-coach
```

### 2. Add All Files

Copy the provided files into their respective locations:
- `pom.xml` → root directory
- `application.properties` → `src/main/resources/`
- All `.java` files → their respective packages in `src/main/java/com/cvcoach/`

### 3. Configure OpenAI API Key

Edit `src/main/resources/application.properties` and replace `your-api-key-here` with your actual OpenAI API key:

```properties
spring.ai.openai.api-key=sk-your-actual-key-here
```

**Alternative:** Set as environment variable:
```bash
export OPENAI_API_KEY=sk-your-actual-key-here
```

### 4. Add Your CV

Place your CV (PDF format) in the `cv/` folder:
```bash
cp /path/to/your/cv.pdf cv/
```

### 5. Build the Project

```bash
mvn clean install
```

### 6. Run the Application

```bash
mvn spring-boot:run
```

Or build and run the JAR:
```bash
mvn clean package
java -jar target/cv-career-coach-1.0.0.jar
```

## Usage

When you run the application, you'll see a menu with 4 options:

```
==================================================
   CV CAREER COACH
   Powered by AI
==================================================

--------------------------------------------------
MAIN MENU
--------------------------------------------------
1 - Read and show skills from CV
2 - Search 3 job positions matching your profile
3 - Search 3 alternative job positions
4 - Exit
--------------------------------------------------
```

### Option 1: Analyze CV
- Reads your CV PDF from the `cv/` folder
- Extracts: location, skills, education, experience
- Saves data to `data/cv_data.csv`
- Displays structured information

### Option 2: Search Matching Jobs
- Finds 3 job positions that match your current profile
- Considers your location, skills, and experience level
- Shows job title, company, requirements, and why it matches

### Option 3: Search Alternative Jobs
- Finds 3 job positions in different fields/locations
- Suggests alternative career paths
- Helps explore new opportunities

### Option 4: Exit
- Closes the application

## Data Storage

Analyzed CV data is stored in: `data/cv_data.csv`

Format:
```csv
Location,Hard Skills,Soft Skills,Education,Total Experience (Years),Job Branch,Branch Experience (Years)
Berlin Germany,"Java;Spring Boot;SQL","English;German","Master Computer Science",5,Software Development,5
```

## Troubleshooting

### "No PDF file found in cv/ folder"
- Make sure you created the `cv/` folder in the project root
- Place your CV PDF file inside it

### "Failed to analyze CV: API key not configured"
- Check your `application.properties` file
- Ensure your OpenAI API key is correct
- Try setting the `OPENAI_API_KEY` environment variable

### "Failed to parse JSON response"
- The AI response format may have changed
- Check logs for the actual response
- The application handles common markdown wrapping automatically

### Maven Build Errors
- Ensure you're using Java 17+: `java -version`
- Clear Maven cache: `mvn clean`
- Update Maven: `mvn -version` (should be 3.6+)

## Dependencies

- **Spring Boot 3.2.0** - Application framework
- **Spring AI 1.0.0-M3** - OpenAI integration
- **Apache PDFBox 3.0.1** - PDF parsing
- **OpenCSV 5.9** - CSV file handling
- **Jackson** - JSON processing
- **Lombok** - Code generation

## API Costs

This application uses OpenAI's API:
- Model: `gpt-4o-mini` (cost-effective)
- Typical usage: ~2-3 API calls per session
- Estimated cost: $0.01-0.05 per session

## Future Enhancements

Potential features for future versions:
- Support for Word documents (.docx)
- Save job searches to file
- Compare multiple job offers
- Generate cover letters
- Track application status
- Web interface

## License

This project is provided as-is for educational purposes.

## Support

For issues with:
- **Spring AI**: https://docs.spring.io/spring-ai/reference/
- **OpenAI API**: https://platform.openai.com/docs
- **This project**: Check logs in console output
