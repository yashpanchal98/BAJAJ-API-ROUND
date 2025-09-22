# Bajaj Finserv Health API Qualifier - Spring Boot Application

## 📋 Project Overview

This Spring Boot application is developed for the **Bajaj Finserv Health API Qualifier Round 1 (JAVA)**. The application automatically executes on startup to:
1. Generate a webhook by sending registration details
2. Solve an assigned SQL problem based on registration number
3. Submit the solution via JWT-authenticated API call

**Key Requirement:** As per the task requirements, this application has **NO controllers or endpoints**. It executes automatically on application startup.

## 🚀 Features

- **Automatic Execution**: Runs immediately on application startup (no manual trigger needed)
- **Webhook Generation**: Sends POST request to Bajaj API to generate webhook
- **Dynamic SQL Selection**: Determines SQL query based on registration number (odd/even)
- **JWT Authentication**: Handles secure API communication with JWT tokens
- **Zero Manual Intervention**: Complete flow executes automatically

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 3.4.10**
- **Spring Web (RestTemplate)**
- **Maven**
- **Jackson (JSON processing)**

## 📦 Prerequisites

- Java 21 or higher
- Maven 3.6+
- Internet connection (required for API calls)

## 🔧 Installation & Setup

### Step 1: Clone the Repository
```bash
git clone https://github.com/yourusername/bajaj-api-round.git
cd bajaj-api-round
```
Step 2: Update Your Registration Details
IMPORTANT: Edit src/main/java/com/bajaj_api_assignment/Bajaj/API/round/service/WebhookService.java
java// Line 20-22 - Replace with YOUR actual details
private static final String YOUR_NAME = "Your Full Name";  // CHANGE THIS
private static final String YOUR_REG_NO = "12345";  // CHANGE THIS  
private static final String YOUR_EMAIL = "your.email@example.com";  // CHANGE THIS
Step 3: Update SQL Query (If Registration is ODD)
The application automatically determines which SQL question to solve:

Registration ends in ODD number → Question 1
Registration ends in EVEN number → Question 2 (already implemented)

If your registration is ODD, update the SQL query in WebhookService.java (around line 130):
java// TODO: UPDATE THIS WITH ACTUAL QUESTION 1 FROM GOOGLE DRIVE LINK
// https://drive.google.com/file/d/1IeSI6l6KoSQAF_fRihIT9tEDICtoz-G/view?usp=sharing
sqlQuery = """
    YOUR ACTUAL QUESTION 1 SQL QUERY HERE
    """;
    
Step 4: Build the Project
bashmvn clean package
Step 5: Run the Application
bash# Option 1: Using Maven
mvn spring-boot:run

# Option 2: Using JAR file
java -jar target/Bajaj-API-round-0.0.1-SNAPSHOT.jar
🔄 Automatic Execution Flow
When you run the application, it automatically:

Starts → Spring Boot application initializes
Triggers WebhookRunner → ApplicationRunner executes immediately
Generates Webhook → Sends POST request with your registration details
Receives Response → Gets webhook URL and JWT access token
Determines SQL Query → Selects Question 1 (odd) or Question 2 (even)
Submits Solution → Sends SQL query to webhook URL with JWT auth
Displays Results → Shows success/failure in console
Keeps Running → Application stays active (check logs)

📊 SQL Problem Assignment

Question Selection Logic
javaLast two digits of registration number:
- ODD (1,3,5,7,9) → Question 1
- EVEN (0,2,4,6,8) → Question 2
Question 2 (Even Numbers) - Already Implemented
Find the highest salary credited NOT on the 1st day of any month:
sqlSELECT 
    p.AMOUNT AS SALARY,
    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME,
    YEAR(CURRENT_DATE) - YEAR(e.DOB) AS AGE,
    d.DEPARTMENT_NAME
FROM PAYMENTS p
INNER JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
INNER JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
WHERE DAY(p.PAYMENT_TIME) != 1
ORDER BY p.AMOUNT DESC
LIMIT 1
Question 1 (Odd Numbers)

```
📁 Project Structure
src/
├── main/
│   ├── java/
│   │   └── com/bajaj_api_assignment/Bajaj/API/round/
│   │       ├── BajajApiRoundApplication.java     # Main application class
│   │       ├── runner/
│   │       │   └── WebhookRunner.java           # Auto-executes on startup
│   │       ├── service/
│   │       │   └── WebhookService.java          # Core business logic
│   │       └── model/
│   │           ├── WebhookRequest.java          # Request model
│   │           ├── WebhookResponse.java         # Response model
│   │           └── SolutionRequest.java         # Solution submission model
│   └── resources/
│       └── application.properties               # Configuration (port 9090)
├── pom.xml                                      # Maven dependencies
└── README.md                                    # This file
```

## ⚙️ Configuration

application.properties:
propertiesspring.application.name=Bajaj-API-round
server.port=9090
logging.level.com.bajaj=DEBUG

🐛 Troubleshooting

Common Issues and Solutions
IssuePossible CauseSolution500 Error from Bajaj APIInvalid registration formatTry different formats: "12345" or "REG12345"401 UnauthorizedJWT token issueCheck console logs, app tries multiple auth formatsApplication doesn't runWrong Java versionEnsure Java 21 is installedNo outputCheck logsLook for errors in console outputConnection refusedNetwork issueCheck internet connection and firewall
Console Output Examples

Successful execution:

========================================
Bajaj Finserv Health API Task

Automatic Execution Starting...

========================================

📡 Step 1: Generating webhook...
✅ Webhook URL received: https://...
🔑 Access Token received: eyJhbGc...
📊 Step 2: Preparing SQL query...
📤 Step 3: Submitting solution...
✨ ALL STEPS COMPLETED SUCCESSFULLY!

========================================
Error example:
❌ Error in webhook flow: 500 Internal Server Error
✅ Submission Checklist
Before submitting, ensure:

 Updated YOUR_NAME, YOUR_REG_NO, YOUR_EMAIL in WebhookService.java
 Updated SQL query for Question 1 (if registration is odd)
 Tested application runs automatically on startup
 Verified successful execution in console logs
 NO controllers or endpoints in the code
 Built JAR file using mvn clean package
 Created GitHub repository with all code
 Added this README.md to repository
 Generated downloadable JAR link
 Submitted form with GitHub and JAR links

📝 Important Notes

No Manual Trigger: The application runs automatically when started. No need for Postman or API calls.
Registration Format: Try different formats if you get errors:

Just numbers: "12345"
With prefix: "REG12345"
As provided by Bajaj Finserv


JWT Authentication: The application automatically tries:

Bearer [token] format first
Direct [token] if Bearer fails


Logs: Check console output for detailed execution flow and any errors.

🤝 Submission Format
GitHub Repository
https://github.com/yashpanchal98/bajaj-api-round.git

JAR File
https://github.com/yashpanchal98/BAJAJ-API-ROUND/raw/main/target/Bajaj-API-round-0.0.1-SNAPSHOT.jar

Quick Start Commands

bash# Clone
git clone <https://github.com/yashpanchal98/BAJAJ-API-ROUND>

# Navigate
cd bajaj-api-round

# Then build
mvn clean package

# Run
java -jar target/Bajaj-API-round-0.0.1-SNAPSHOT.jar
