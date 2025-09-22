package com.bajaj_api_assignment.Bajaj.API.round;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BajajApiRoundApplication {

    public static void main(String[] args) {
        SpringApplication.run(BajajApiRoundApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
