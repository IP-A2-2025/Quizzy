package com.backend.config;

import com.backend.service.GeminiService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class GeminiTestConfig {

    @Bean
    public GeminiService geminiService() {
        return Mockito.mock(GeminiService.class);
    }
}
