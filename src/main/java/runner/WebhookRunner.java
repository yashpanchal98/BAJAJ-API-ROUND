package com.bajaj_api_assignment.Bajaj.API.round.runner;

import com.bajaj_api_assignment.Bajaj.API.round.service.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class WebhookRunner implements ApplicationRunner {

    @Autowired
    private WebhookService webhookService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        webhookService.executeWebhookFlow();
    }
}