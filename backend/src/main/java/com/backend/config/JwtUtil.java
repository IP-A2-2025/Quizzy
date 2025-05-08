package com.backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Base64;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    
    // Secret key that meets the minimum 256-bit requirement for HS256
    private static final String SECRET = "secret-discret-secret-discret-secret-discret-secret-discret";
    private static final byte[] KEY_BYTES = SECRET.getBytes();
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(KEY_BYTES);
    private static final long EXPIRATION_TIME = 86400000; // 24 hours in milliseconds

    public String generateToken(String email) {
        try {
            logger.info("Generating token for email: {}", email);
            if (email == null || email.isEmpty()) {
                logger.error("Email is null or empty");
                throw new IllegalArgumentException("Email cannot be null or empty");
            }

            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

            logger.debug("Token generation details - Now: {}, Expiry: {}", now, expiryDate);
            
            String token = Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(SECRET_KEY)
                    .compact();

            logger.info("Token generated successfully for email: {}", email);
            logger.debug("Generated token: {}", token);
            return token;
        } catch (Exception e) {
            logger.error("Error generating token for email: {}. Error: {}", email, e.getMessage(), e);
            throw new RuntimeException("Failed to generate token: " + e.getMessage(), e);
        }
    }

    public String getSubjectFromToken(String token) {
        try {
            logger.info("Extracting subject from token");
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            String subject = claims.getSubject();
            logger.info("Successfully extracted subject: {}", subject);
            return subject;
        } catch (Exception e) {
            logger.error("Error extracting subject from token: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid token: " + e.getMessage(), e);
        }
    }

    public boolean validateToken(String token) {
        try {
            logger.info("Validating token");
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            logger.info("Token validation successful");
            return true;
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage(), e);
            return false;
        }
    }
}
