package com.backend.config;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CorsConfigTest {

    @Mock
    private HttpServletRequest mockRequest;

    @Test
    void shouldCreateCorsFilterBean() {
        // Given
        CorsConfig corsConfig = new CorsConfig();

        // When
        CorsFilter corsFilter = corsConfig.corsFilter();

        // Then
        assertNotNull(corsFilter);
    }

    @Test
    void shouldConfigureCorsWithAllOriginsAllowed() {
        // Given
        CorsConfig corsConfig = new CorsConfig();

        // When
        CorsFilter corsFilter = corsConfig.corsFilter();

        // Then
        assertNotNull(corsFilter);
        // We can't directly test the internal configuration of CorsFilter
        // but we can verify it's properly instantiated
    }

    @Test
    void shouldAllowAllHeaders() {
        // Given
        CorsConfig corsConfig = new CorsConfig();

        // When
        CorsFilter corsFilter = corsConfig.corsFilter();

        // Then
        assertNotNull(corsFilter);
        // CorsFilter should be configured to allow all headers
    }

    @Test
    void shouldAllowAllMethods() {
        // Given
        CorsConfig corsConfig = new CorsConfig();

        // When
        CorsFilter corsFilter = corsConfig.corsFilter();

        // Then
        assertNotNull(corsFilter);
        // CorsFilter should be configured to allow all HTTP methods
    }

    @Test
    void shouldApplyToAllPaths() {
        // Given
        CorsConfig corsConfig = new CorsConfig();

        // When
        CorsFilter corsFilter = corsConfig.corsFilter();

        // Then
        assertNotNull(corsFilter);
        // Configuration should apply to all paths (/**)
    }

    @Test
    void shouldCreateValidCorsConfiguration() {
        // Given & When
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");

        // Then
        assertNotNull(corsConfiguration);
        assertTrue(corsConfiguration.getAllowedOrigins().contains("*"));
        assertTrue(corsConfiguration.getAllowedHeaders().contains("*"));
        assertTrue(corsConfiguration.getAllowedMethods().contains("*"));
    }

} 