// File: src/main/java/com/bajaj_api_assignment/Bajaj/API/round/controller/TestController.java
package com.bajaj_api_assignment.Bajaj.API.round.controller;

import com.bajaj_api_assignment.Bajaj.API.round.service.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private WebhookService webhookService;

    // Test endpoint to trigger the webhook flow manually
    @PostMapping("/trigger-webhook")
    public ResponseEntity<Map<String, String>> triggerWebhook() {
        Map<String, String> response = new HashMap<>();
        try {
            System.out.println("Manual trigger initiated via API...");
            webhookService.executeWebhookFlow();

            response.put("status", "success");
            response.put("message", "Webhook flow completed successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());
            response.put("details", e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Test endpoint with custom registration details
    @PostMapping("/trigger-webhook-custom")
    public ResponseEntity<Map<String, String>> triggerWebhookCustom(@RequestBody RegistrationDetails details) {
        Map<String, String> response = new HashMap<>();
        try {
            System.out.println("Manual trigger with custom details...");
            System.out.println("Name: " + details.getName());
            System.out.println("RegNo: " + details.getRegNo());
            System.out.println("Email: " + details.getEmail());

            // Call the service with custom details
            webhookService.executeWebhookFlowWithDetails(
                    details.getName(),
                    details.getRegNo(),
                    details.getEmail()
            );

            response.put("status", "success");
            response.put("message", "Webhook flow completed successfully with custom details");
            response.put("regNo", details.getRegNo());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());
            response.put("details", e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Service is running on port 9090");
        return ResponseEntity.ok(response);
    }

    // Get SQL query without executing
    @GetMapping("/get-sql/{regNo}")
    public ResponseEntity<Map<String, Object>> getSqlQuery(@PathVariable String regNo) {
        Map<String, Object> response = new HashMap<>();
        try {
            String query = webhookService.getSQLQueryForTesting(regNo);

            // Determine if odd or even
            String numbersOnly = regNo.replaceAll("[^0-9]", "");
            String lastTwoDigits = numbersOnly.length() >= 2
                    ? numbersOnly.substring(numbersOnly.length() - 2)
                    : numbersOnly;
            int lastNumber = Integer.parseInt(lastTwoDigits);
            boolean isOdd = lastNumber % 2 != 0;

            response.put("regNo", regNo);
            response.put("lastTwoDigits", lastTwoDigits);
            response.put("questionType", isOdd ? "Question 1 (Odd)" : "Question 2 (Even)");
            response.put("sqlQuery", query);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    static class RegistrationDetails {
        private String name;
        private String regNo;
        private String email;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getRegNo() { return regNo; }
        public void setRegNo(String regNo) { this.regNo = regNo; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}