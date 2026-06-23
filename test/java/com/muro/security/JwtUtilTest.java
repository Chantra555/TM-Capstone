package com.muro.security;

import com.muro.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private UserDetails userDetails;
    private static final String TEST_USERNAME = "testuser@example.com";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        userDetails = User.withUsername(TEST_USERNAME)
                .password("password")
                .authorities(Collections.emptyList())
                .build();
    }

    // =========================
    // generateToken()
    // =========================

    @Test
    void generateToken_ShouldReturnNonNullToken() {
        String token = jwtUtil.generateToken(TEST_USERNAME);
        assertNotNull(token);
    }

    @Test
    void generateToken_ShouldReturnThreePartJwt() {
        String token = jwtUtil.generateToken(TEST_USERNAME);
        // JWT format: header.payload.signature
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length, "JWT should have exactly 3 parts");
    }

    @Test
    void generateToken_DifferentUsernames_ShouldReturnDifferentTokens() {
        String token1 = jwtUtil.generateToken("user1");
        String token2 = jwtUtil.generateToken("user2");
        assertNotEquals(token1, token2);
    }

    // =========================
    // extractUsername()
    // =========================

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        String token = jwtUtil.generateToken(TEST_USERNAME);
        String extracted = jwtUtil.extractUsername(token);
        assertEquals(TEST_USERNAME, extracted);
    }

    @Test
    void extractUsername_WithSpecialCharacters_ShouldReturnCorrectUsername() {
        String specialUsername = "user+tag@domain.co.uk";
        String token = jwtUtil.generateToken(specialUsername);
        assertEquals(specialUsername, jwtUtil.extractUsername(token));
    }

    // =========================
    // extractExpiration()
    // =========================

    @Test
    void extractExpiration_ShouldReturnFutureDate() {
        String token = jwtUtil.generateToken(TEST_USERNAME);
        Date expiration = jwtUtil.extractExpiration(token);
        assertTrue(expiration.after(new Date()), "Expiration should be in the future");
    }

    @Test
    void extractExpiration_ShouldBeApproximately24HoursFromNow() {
        long before = System.currentTimeMillis();
        String token = jwtUtil.generateToken(TEST_USERNAME);
        long after = System.currentTimeMillis();

        Date expiration = jwtUtil.extractExpiration(token);
        long expirationMillis = expiration.getTime();

        // Allow ±1 second tolerance
        assertTrue(expirationMillis >= before + 86400000 - 1000);
        assertTrue(expirationMillis <= after + 86400000 + 1000);
    }

    // =========================
    // isTokenExpired()
    // =========================

    @Test
    void isTokenExpired_WithFreshToken_ShouldReturnFalse() {
        String token = jwtUtil.generateToken(TEST_USERNAME);
        assertFalse(jwtUtil.isTokenExpired(token));
    }

    @Test
    void isTokenExpired_WithManuallyExpiredToken_ShouldThrowOrReturnTrue() {
        // Pre-built expired token (signed with same secret, expiry in the past)
        // Generated with exp = epoch 1 (Jan 1, 1970)
        // You can generate this once and hard-code it for deterministic testing
        String expiredToken = buildExpiredToken();

        // isTokenExpired internally calls extractExpiration which parses the JWT.
        // jjwt throws ExpiredJwtException on parse if token is expired.
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.isTokenExpired(expiredToken));
    }

    // =========================
    // isTokenValid()
    // =========================

    @Test
    void isTokenValid_WithMatchingUsernameAndFreshToken_ShouldReturnTrue() {
        String token = jwtUtil.generateToken(TEST_USERNAME);
        assertTrue(jwtUtil.isTokenValid(token, userDetails));
    }

    @Test
    void isTokenValid_WithWrongUsername_ShouldReturnFalse() {
        String token = jwtUtil.generateToken("other_user");
        assertFalse(jwtUtil.isTokenValid(token, userDetails));
    }

    @Test
    void isTokenValid_WithTamperedToken_ShouldThrowException() {
        String token = jwtUtil.generateToken(TEST_USERNAME);
        String tampered = token.substring(0, token.length() - 5) + "XXXXX";
        assertThrows(Exception.class, () -> jwtUtil.isTokenValid(tampered, userDetails));
    }

    @Test
    void isTokenValid_WithEmptyToken_ShouldThrowException() {
        assertThrows(Exception.class, () -> jwtUtil.isTokenValid("", userDetails));
    }

    @Test
    void isTokenValid_WithMalformedToken_ShouldThrowException() {
        assertThrows(Exception.class, () -> jwtUtil.isTokenValid("not.a.jwt", userDetails));
    }

    // =========================
    // Helper
    // =========================

    /**
     * Builds a token that is already expired using the same secret key.
     * Uses Jwts.builder() directly since JwtUtil doesn't expose expiry control.
     */
    private String buildExpiredToken() {
        io.jsonwebtoken.security.Keys.class.getName(); // ensure import resolved
        javax.crypto.SecretKey key = io.jsonwebtoken.security.Keys
                .hmacShaKeyFor(io.jsonwebtoken.io.Decoders.BASE64.decode(
                        "/gAUM8M6xNEauXRppgZZ1FSxH+RQ40O+d/9wrlVSO38="));

        return io.jsonwebtoken.Jwts.builder()
                .subject(TEST_USERNAME)
                .issuedAt(new Date(1000))
                .expiration(new Date(2000))   // expired in 1970
                .signWith(key)
                .compact();
    }
}