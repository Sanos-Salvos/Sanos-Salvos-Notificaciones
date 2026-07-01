package com.sanos.notificaciones.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    private final SecurityConfig config = new SecurityConfig();

    @Test
    void passwordEncoder_returnsBCrypt() {
        var encoder = config.passwordEncoder();
        assertNotNull(encoder);
        assertInstanceOf(BCryptPasswordEncoder.class, encoder);
    }

    @Test
    void passwordEncoder_encodesAndMatches() {
        var encoder = config.passwordEncoder();
        String raw = "miPassword123";
        String encoded = encoder.encode(raw);
        assertNotEquals(raw, encoded);
        assertTrue(encoder.matches(raw, encoded));
    }

    @Test
    void passwordEncoder_doesNotMatchWrongPassword() {
        var encoder = config.passwordEncoder();
        String encoded = encoder.encode("correct");
        assertFalse(encoder.matches("wrong", encoded));
    }
}
