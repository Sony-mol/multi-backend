package com.example.demo.config;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridConfig {

    @Value("${SENDGRID_API_KEY:}")
    private String sendGridApiKey;

    @Bean
    public SendGrid sendGrid() {
        if (sendGridApiKey == null || sendGridApiKey.trim().isEmpty()) {
            throw new IllegalStateException("SENDGRID_API_KEY environment variable is not set");
        }
        return new SendGrid(sendGridApiKey);
    }
}
