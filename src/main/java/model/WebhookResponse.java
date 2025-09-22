package com.bajaj_api_assignment.Bajaj.API.round.model;

public class WebhookResponse {
    private String webhook;
    private String accessToken;

    public WebhookResponse() {}

    public WebhookResponse(String webhook, String accessToken) {
        this.webhook = webhook;
        this.accessToken = accessToken;
    }

    // Getters and Setters
    public String getWebhook() { return webhook; }
    public void setWebhook(String webhook) { this.webhook = webhook; }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
}