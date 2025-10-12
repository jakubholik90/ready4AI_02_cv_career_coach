# Example Console Output

This document shows what you can expect to see when running the CV Career Coach application.

## Application Start

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
Enter your choice (1-4): 
```

## Option 1: Read and Show Skills from CV

**User Input:** `1`

```
[Processing CV...]

=== CV Analysis Results ===
Location: Berlin, Germany
Hard Skills: [Java, Spring Boot, Python, SQL, Docker, Kubernetes]
Soft Skills: [English, German, Team Leadership, Problem Solving]
Education: Master of Science in Computer Science
Total Experience: 5 years
Current Job Branch: Software Development
Years in Branch: 5 years
===========================

✓ CV data saved to data/cv_data.csv

--------------------------------------------------
MAIN MENU
--------------------------------------------------
1 - Read and show skills from CV
2 - Search 3 job positions matching your profile
3 - Search 3 alternative job positions
4 - Exit
--------------------------------------------------
Enter your choice (1-4): 
```

## Option 2: Search Matching Job Positions

**User Input:** `2`

```
[Searching for matching job positions...]

==================================================
  FOUND 3 JOB POSITIONS
==================================================

[1] 
--- Job Position ---
Title: Senior Java Developer
Company: Tech Solutions GmbH
Location: Berlin, Germany
Required Skills: Java, Spring Boot, Microservices, Docker
Experience Level: Senior
Description: Leading development team building cloud-native applications using Java and Spring Boot. Work on scalable microservices architecture.
Match Reason: Perfect match for your Java/Spring Boot expertise and 5 years experience. Location matches your current city.
-------------------

[2] 
--- Job Position ---
Title: Backend Engineer
Company: FinTech Startups
Location: Berlin, Germany
Required Skills: Java, Python, SQL, Kubernetes
Experience Level: Mid-Senior
Description: Develop backend services for financial applications. Work with modern cloud technologies and agile methodologies.
Match Reason: Matches your full-stack skills including Python and Kubernetes. Your experience level fits the senior role.
-------------------

[3] 
--- Job Position ---
Title: Software Architect
Company: E-Commerce Companies
Location: Munich, Germany
Required Skills: Java, System Design, Docker, Team Leadership
Experience Level: Senior
Description: Design and implement scalable e-commerce platforms. Lead technical decisions and mentor development teams.
Match Reason: Your team leadership skills and technical expertise make you ideal for an architect role. Still in Germany.
-------------------

--------------------------------------------------
MAIN MENU
--------------------------------------------------
```

## Option 3: Search Alternative Job Positions

**User Input:** `3`

```
[Searching for alternative job positions...]

==================================================
  FOUND 3 JOB POSITIONS
==================================================

[1] 
--- Job Position ---
Title: Product Manager - Technical
Company: SaaS Companies
Location: Amsterdam, Netherlands
Required Skills: Technical background, Product strategy, Stakeholder management
Experience Level: Mid-Senior
Description: Bridge technical and business teams to deliver innovative SaaS products. Your development background provides credibility with engineering teams.
Match Reason: Career transition leveraging your technical expertise into product management. New location and field.
-------------------

[2] 
--- Job Position ---
Title: DevOps Consultant
Company: Cloud Consulting Firms
Location: Remote / Vienna, Austria
Required Skills: Docker, Kubernetes, Cloud platforms, Client communication
Experience Level: Senior
Description: Help clients migrate to cloud-native architectures. Travel to client sites and lead implementation projects.
Match Reason: Uses your Docker/Kubernetes skills but in a consulting role. International exposure and different work style.
-------------------

[3] 
--- Job Position ---
Title: Technical Trainer
Company: Educational Technology Companies
Location: London, UK
Required Skills: Programming expertise, Communication, Curriculum development
Experience Level: Mid
Description: Create and deliver programming courses for corporate clients. Combine your technical skills with teaching.
Match Reason: Complete career change using your technical knowledge. Your multilingual skills (English/German) are valuable.
-------------------

--------------------------------------------------
MAIN MENU
--------------------------------------------------
```

## Option 4: Exit

**User Input:** `4`

```
Thank you for using CV Career Coach. Goodbye!
```

## Error Examples

### No CV File Found

```
Enter your choice (1-4): 1

[Processing CV...]

✗ Failed to analyze CV: No PDF file found in cv/ folder. Please place your CV PDF there.
  Make sure:
  1. You have placed your CV PDF in the 'cv' folder
  2. Your OpenAI API key is configured in application.properties
```

### No CV Data for Job Search

```
Enter your choice (1-4): 2

⚠ No CV data available.
  Please analyze your CV first (Option 1).
```

### API Key Not Configured

```
[Processing CV...]

✗ Failed to analyze CV: API key not configured
  Make sure:
  1. You have placed your CV PDF in the 'cv' folder
  2. Your OpenAI API key is configured in application.properties
```

## Generated CSV File

**File:** `data/cv_data.csv`

```csv
Location,Hard Skills,Soft Skills,Education,Total Experience (Years),Job Branch,Branch Experience (Years)
"Berlin, Germany","Java;Spring Boot;Python;SQL;Docker;Kubernetes","English;German;Team Leadership;Problem Solving","Master of Science in Computer Science",5,"Software Development",5
```