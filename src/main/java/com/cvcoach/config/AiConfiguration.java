package com.cvcoach.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class AiConfiguration {

    /**
     * Provides ChatClient.Builder bean for AI services
     */
    @Bean
    public ChatClient.Builder chatClientBuilder(@Lazy ChatClient.Builder builder) {
        return builder;
    }

    /**
     * Provides ObjectMapper for JSON parsing
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
