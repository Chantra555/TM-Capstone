package com.muro.controller;

import com.muro.controller.AuthController.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        loginResponse = new LoginResponse("my.jwt.token", 1L, "john@example.com");
    }

    // =========================
    // LoginResponse - getToken()
    // =========================

    @Test
    void loginResponse_getToken_ShouldReturnCorrectToken() {
        assertEquals("my.jwt.token", loginResponse.getToken());
    }

    @Test
    void loginResponse_getToken_ShouldNotBeNull() {
        assertNotNull(loginResponse.getToken());
    }

    @Test
    void loginResponse_getToken_ShouldNotBeEmpty() {
        assertFalse(loginResponse.getToken().isEmpty());
    }

    // =========================
    // LoginResponse - getUserId()
    // =========================

    @Test
    void loginResponse_getUserId_ShouldReturnCorrectId() {
        assertEquals(1L, loginResponse.getUserId());
    }

    @Test
    void loginResponse_getUserId_ShouldNotBeNull() {
        assertNotNull(loginResponse.getUserId());
    }

    @Test
    void loginResponse_getUserId_ShouldBePositive() {
        assertTrue(loginResponse.getUserId() > 0);
    }

    // =========================
    // LoginResponse - getUsername()
    // =========================

    @Test
    void loginResponse_getUsername_ShouldReturnCorrectUsername() {
        assertEquals("john@example.com", loginResponse.getUsername());
    }

    @Test
    void loginResponse_getUsername_ShouldNotBeNull() {
        assertNotNull(loginResponse.getUsername());
    }

    @Test
    void loginResponse_getUsername_ShouldNotBeEmpty() {
        assertFalse(loginResponse.getUsername().isEmpty());
    }

    // =========================
    // LoginResponse - Constructor variations
    // =========================

    @Test
    void loginResponse_WithNullToken_ShouldStoreNull() {
        LoginResponse response = new LoginResponse(null, 1L, "john@example.com");
        assertNull(response.getToken());
    }

    @Test
    void loginResponse_WithNullUsername_ShouldStoreNull() {
        LoginResponse response = new LoginResponse("token", 1L, null);
        assertNull(response.getUsername());
    }

    @Test
    void loginResponse_WithNullUserId_ShouldStoreNull() {
        LoginResponse response = new LoginResponse("token", null, "john@example.com");
        assertNull(response.getUserId());
    }

    @Test
    void loginResponse_AllFieldsSetCorrectly_ShouldMatchExpected() {
        LoginResponse response = new LoginResponse("abc.def.ghi", 99L, "jane@example.com");

        assertAll("All LoginResponse fields",
                () -> assertEquals("abc.def.ghi", response.getToken()),
                () -> assertEquals(99L,           response.getUserId()),
                () -> assertEquals("jane@example.com", response.getUsername())
        );
    }
}