package com.bajaj_api_assignment.Bajaj.API.round.service;

import com.bajaj_api_assignment.Bajaj.API.round.model.WebhookRequest;
import com.bajaj_api_assignment.Bajaj.API.round.model.WebhookResponse;
import com.bajaj_api_assignment.Bajaj.API.round.model.SolutionRequest;
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

    // TODO: UPDATE THESE WITH YOUR ACTUAL DETAILS
    private static final String YOUR_NAME = "Yash Panchal";  // Change this
    private static final String YOUR_REG_NO = "0101CS221153";  // Change this to your registration number
    private static final String YOUR_EMAIL = "pclyash@gmail.com";  // Change this

    private static final String GENERATE_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
    private static final String SUBMIT_WEBHOOK_BASE_URL = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";

    public void executeWebhookFlow() {
        try {
            // Step 1: Generate webhook
            System.out.println("\n📡 Step 1: Generating webhook...");
            WebhookResponse webhookResponse = generateWebhook();

            if (webhookResponse == null || webhookResponse.getWebhook() == null) {
                System.err.println(" Failed to generate webhook - No response received");
                return;
            }

            System.out.println(" Webhook URL received: " + webhookResponse.getWebhook());
            System.out.println(" Access Token received: " + maskToken(webhookResponse.getAccessToken()));


            System.out.println("\n Step 2: Determining SQL query based on registration number...");
            String finalQuery = getSQLQuery(YOUR_REG_NO);
            System.out.println(" SQL Query prepared");


            System.out.println("\n Step 3: Submitting solution to webhook...");
            submitSolution(webhookResponse.getWebhook(), webhookResponse.getAccessToken(), finalQuery);

        } catch (Exception e) {
            System.err.println("\n Error during webhook flow: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private WebhookResponse generateWebhook() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            WebhookRequest request = new WebhookRequest(YOUR_NAME, YOUR_REG_NO, YOUR_EMAIL);

            System.out.println("Request Details: Name=" + YOUR_NAME + ", RegNo=" + YOUR_REG_NO + ", Email=" + YOUR_EMAIL);

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
            System.err.println("No numbers found in registration number!");
            numbersOnly = "00";
        }

        String lastTwoDigits = numbersOnly.length() >= 2
                ? numbersOnly.substring(numbersOnly.length() - 2)
                : numbersOnly;

        int lastNumber = Integer.parseInt(lastTwoDigits);
        boolean isOdd = lastNumber % 2 != 0;

        System.out.println("Registration number: " + regNo);
        System.out.println("Last two digits: " + lastTwoDigits);
        System.out.println("Question type: " + (isOdd ? "Question 1 (Odd)" : "Question 2 (Even)"));

        String sqlQuery;

        if (!isOdd) {
            // Question 2 (Even numbers) - Based on the PDF provided
            // Find highest salary NOT credited on 1st of any month
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
            // Question 1 (Odd numbers) - You need to check the Google Drive link for this
            // This is a placeholder - UPDATE THIS based on Question 1 requirements
            System.out.println("Using Question 1 query - Please verify this matches the requirements!");

            // Example placeholder query - REPLACE with actual Question 1 requirements
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
        }

        System.out.println("SQL Query: \n" + sqlQuery);
        return sqlQuery.trim();
    }

    private void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", accessToken);  // Using the token directly as JWT

            SolutionRequest request = new SolutionRequest(sqlQuery);

            HttpEntity<SolutionRequest> entity = new HttpEntity<>(request, headers);

            // Use the webhook URL from the response
            System.out.println("Submitting to: " + webhookUrl);

            ResponseEntity<String> response = restTemplate.exchange(
                    webhookUrl,  // Use the webhook URL received from first API
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            System.out.println(" Solution submitted successfully!");
            System.out.println("Response Status: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());

        } catch (HttpClientErrorException e) {
            System.err.println("Client Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw e;
        } catch (HttpServerErrorException e) {
            System.err.println("Server Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            System.err.println("Error submitting solution: " + e.getMessage());
            throw e;
        }
    }

    private String maskToken(String token) {
        if (token == null || token.length() < 10) return token;
        return token.substring(0, 10) + "..." + token.substring(token.length() - 5);
    }
}