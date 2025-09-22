package com.bajaj_api_assignment.Bajaj.API.round.runner;

import com.bajaj_api_assignment.Bajaj.API.round.service.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component  // IMPORTANT: This ensures automatic execution on startup
public class WebhookRunner implements ApplicationRunner {

    @Autowired
    private WebhookService webhookService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("========================================");
        System.out.println("Bajaj Finserv Health API Task");
        System.out.println("Automatic Execution Starting...");
        System.out.println("========================================");

        try {
            webhookService.executeWebhookFlow();
            System.out.println("\n✅ Task completed successfully!");
        } catch (Exception e) {
            System.err.println("\n❌ Task failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}