package com.backend.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;

class SecurityFilterChainTest {

    @Test
    void securityConfigShouldNotBeNull() {
        // Just verify the config class can be instantiated
        SecurityConfig securityConfig = new SecurityConfig();
        assertNotNull(securityConfig);
    }

    @Test
    void passwordEncoderShouldBeConfiguredCorrectly() {
        // Create instance of the security config
        SecurityConfig securityConfig = new SecurityConfig();

        // Test that password encoder is correctly configured
        assertNotNull(securityConfig.passwordEncoder());
        assertTrue(securityConfig.passwordEncoder().matches("password",
                securityConfig.passwordEncoder().encode("password")));
    }
}