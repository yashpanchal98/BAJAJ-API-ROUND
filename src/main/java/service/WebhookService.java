package com.bajaj_api_assignment.Bajaj.API.round.service;

import com.bajaj_api_assignment.Bajaj.API.round.model.WebhookRequest;
import com.bajaj_api_assignment.Bajaj.API.round.model.WebhookResponse;
import com.bajaj_api_assignment.Bajaj.API.round.model.SolutionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Service
public class WebhookService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // TODO: UPDATE THESE WITH YOUR ACTUAL DETAILS BEFORE SUBMISSION
    private static final String YOUR_NAME = "Yash Panchal";  // CHANGE THIS
    private static final String YOUR_REG_NO = "0101CS221153";  // CHANGE THIS
    private static final String YOUR_EMAIL = "pclyash@gmail.com";  // CHANGE THIS

    private static final String GENERATE_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    public void executeWebhookFlow() {
        try {
            // Step 1: Generate webhook
            System.out.println("\n📡 Step 1: Generating webhook...");
            System.out.println("Using registration: " + YOUR_REG_NO);

            WebhookResponse webhookResponse = generateWebhook();

            if (webhookResponse == null || webhookResponse.getWebhook() == null) {
                throw new RuntimeException("Failed to generate webhook - No response received");
            }

            System.out.println("✅ Webhook URL received: " + webhookResponse.getWebhook());
            System.out.println("🔑 Access Token received: " + maskToken(webhookResponse.getAccessToken()));

            // Step 2: Determine SQL query based on registration number
            System.out.println("\n📊 Step 2: Preparing SQL query...");
            String finalQuery = getSQLQuery(YOUR_REG_NO);

            // Step 3: Submit solution
            System.out.println("\n📤 Step 3: Submitting solution...");
            submitSolution(webhookResponse.getWebhook(), webhookResponse.getAccessToken(), finalQuery);

            System.out.println("\n========================================");
            System.out.println("✨ ALL STEPS COMPLETED SUCCESSFULLY!");
            System.out.println("========================================");

        } catch (Exception e) {
            System.err.println("\n❌ Error in webhook flow: " + e.getMessage());
            throw new RuntimeException("Webhook flow failed: " + e.getMessage(), e);
        }
    }

    private WebhookResponse generateWebhook() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            WebhookRequest request = new WebhookRequest(YOUR_NAME, YOUR_REG_NO, YOUR_EMAIL);

            System.out.println("Sending request with:");
            System.out.println("  Name: " + YOUR_NAME);
            System.out.println("  RegNo: " + YOUR_REG_NO);
            System.out.println("  Email: " + YOUR_EMAIL);

            HttpEntity<WebhookRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<WebhookResponse> response = restTemplate.exchange(
                    GENERATE_WEBHOOK_URL,
                    HttpMethod.POST,
                    entity,
                    WebhookResponse.class
            );

            System.out.println("Response Status: " + response.getStatusCode());
            return response.getBody();

        } catch (HttpClientErrorException e) {
            System.err.println("Client Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw e;
        } catch (HttpServerErrorException e) {
            System.err.println("Server Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            System.err.println("Error generating webhook: " + e.getMessage());
            throw e;
        }
    }

    private String getSQLQuery(String regNo) {
        // Extract last two digits from registration number
        String numbersOnly = regNo.replaceAll("[^0-9]", "");

        if (numbersOnly.isEmpty()) {
            System.err.println("Warning: No numbers found in registration number!");
            numbersOnly = "00";
        }

        String lastTwoDigits = numbersOnly.length() >= 2
                ? numbersOnly.substring(numbersOnly.length() - 2)
                : String.format("%02d", Integer.parseInt(numbersOnly));

        int lastNumber = Integer.parseInt(lastTwoDigits);
        boolean isOdd = lastNumber % 2 != 0;

        System.out.println("Registration: " + regNo);
        System.out.println("Last two digits: " + lastTwoDigits);
        System.out.println("Question assigned: " + (isOdd ? "Question 1 (Odd)" : "Question 2 (Even)"));

        String sqlQuery;

        if (!isOdd) {
            // Question 2 (Even numbers) - From the PDF provided
            System.out.println("Using Question 2 SQL query");
            sqlQuery = """
                SELECT 
                    p.AMOUNT AS SALARY,
                    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME,
                    YEAR(CURRENT_DATE) - YEAR(e.DOB) AS AGE,
                    d.DEPARTMENT_NAME
                FROM PAYMENTS p
                INNER JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
                INNER JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
                WHERE DAY(p.PAYMENT_TIME) != 1
                ORDER BY p.AMOUNT DESC
                LIMIT 1""";
        } else {
            // Question 1 (Odd numbers)
            System.out.println("Using Question 1 SQL query");

            // TODO: UPDATE THIS WITH ACTUAL QUESTION 1 FROM GOOGLE DRIVE LINK
            // https://drive.google.com/file/d/1IeSI6l6KoSQAF_fRihIT9tEDICtoz-G/view?usp=sharing

            // PLACEHOLDER - MUST BE UPDATED WITH ACTUAL QUESTION 1 REQUIREMENTS
            sqlQuery = """
                SELECT 
                    p.AMOUNT AS SALARY,
                    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME,
                    YEAR(CURRENT_DATE) - YEAR(e.DOB) AS AGE,
                    d.DEPARTMENT_NAME
                FROM PAYMENTS p
                INNER JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
                INNER JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
                WHERE DAY(p.PAYMENT_TIME) = 1
                ORDER BY p.AMOUNT DESC
                LIMIT 1""";

            System.out.println("⚠️ WARNING: Using placeholder for Question 1 - UPDATE THIS!");
        }

        System.out.println("SQL Query prepared: \n" + sqlQuery);
        return sqlQuery.trim();
    }

    private void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Try with Bearer prefix first (most common JWT format)
            headers.set("Authorization", "Bearer " + accessToken);

            SolutionRequest request = new SolutionRequest(sqlQuery);

            System.out.println("Submitting to: " + webhookUrl);
            System.out.println("With Authorization: Bearer [token]");

            HttpEntity<SolutionRequest> entity = new HttpEntity<>(request, headers);

            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        webhookUrl,
                        HttpMethod.POST,
                        entity,
                        String.class
                );

                System.out.println("✅ Solution submitted successfully!");
                System.out.println("Response Status: " + response.getStatusCode());
                System.out.println("Response Body: " + response.getBody());

            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    System.out.println("Bearer token failed, trying without prefix...");

                    // Try without Bearer prefix
                    headers.set("Authorization", accessToken);
                    HttpEntity<SolutionRequest> entity2 = new HttpEntity<>(request, headers);

                    ResponseEntity<String> response = restTemplate.exchange(
                            webhookUrl,
                            HttpMethod.POST,
                            entity2,
                            String.class
                    );

                    System.out.println("✅ Solution submitted successfully (without Bearer)!");
                    System.out.println("Response: " + response.getBody());
                } else {
                    throw e;
                }
            }

        } catch (HttpClientErrorException e) {
            System.err.println("Client Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            System.err.println("Error submitting solution: " + e.getMessage());
            throw e;
        }
    }

    private String maskToken(String token) {
        if (token == null || token.length() < 20) return token;
        return token.substring(0, 10) + "..." + token.substring(token.length() - 5);
    }
}
