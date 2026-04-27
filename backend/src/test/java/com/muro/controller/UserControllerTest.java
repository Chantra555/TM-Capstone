package com.muro.controller;

import com.muro.dto.UserResponse;
import com.muro.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private User user;
    private UserResponse userResponse;
    public void setId(Long id) {
        this.id = id;
    }

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("john@example.com");
        user.setName("John Doe");

        userResponse = new UserResponse(1L, "john@example.com", "John Doe");
    }

    // =========================
    // User Entity - Getters
    // =========================

    @Test
    void user_getId_ShouldReturnCorrectId() {
        assertEquals(1L, user.getId());
    }

    @Test
    void user_getUsername_ShouldReturnCorrectUsername() {
        assertEquals("john@example.com", user.getUsername());
    }

    @Test
    void user_getName_ShouldReturnCorrectName() {
        assertEquals("John Doe", user.getName());
    }

    @Test
    void user_getAllFields_ShouldMatchExpected() {
        assertAll("All User fields",
                () -> assertEquals(1L,                  user.getId()),
                () -> assertEquals("john@example.com",  user.getUsername()),
                () -> assertEquals("John Doe",          user.getName())
        );
    }

    // =========================
    // User Entity - Setters
    // =========================

    @Test
    void user_setUsername_ShouldUpdateValue() {
        user.setUsername("jane@example.com");
        assertEquals("jane@example.com", user.getUsername());
    }

    @Test
    void user_setName_ShouldUpdateValue() {
        user.setName("Jane Doe");
        assertEquals("Jane Doe", user.getName());
    }

    @Test
    void user_setId_ShouldUpdateValue() {
        user.setId(99L);
        assertEquals(99L, user.getId());
    }

    // =========================
    // User Entity - Null / Edge Cases
    // =========================

    @Test
    void user_WithNullUsername_ShouldStoreNull() {
        user.setUsername(null);
        assertNull(user.getUsername());
    }

    @Test
    void user_WithNullName_ShouldStoreNull() {
        user.setName(null);
        assertNull(user.getName());
    }

    // =========================
    // UserResponse DTO - Getters
    // =========================

    @Test
    void userResponse_getId_ShouldReturnCorrectId() {
        assertEquals(1L, userResponse.getId());
    }

    @Test
    void userResponse_getUsername_ShouldReturnCorrectUsername() {
        assertEquals("john@example.com", userResponse.getUsername());
    }

    @Test
    void userResponse_getName_ShouldReturnCorrectName() {
        assertEquals("John Doe", userResponse.getName());
    }

    @Test
    void userResponse_getAllFields_ShouldMatchExpected() {
        assertAll("All UserResponse fields",
                () -> assertEquals(1L,                  userResponse.getId()),
                () -> assertEquals("john@example.com",  userResponse.getUsername()),
                () -> assertEquals("John Doe",          userResponse.getName())
        );
    }

    // =========================
    // UserResponse DTO - Null / Edge Cases
    // =========================

    @Test
    void userResponse_WithNullName_ShouldStoreNull() {
        UserResponse response = new UserResponse(1L, "john@example.com", null);
        assertNull(response.getName());
    }

    @Test
    void userResponse_WithNullUsername_ShouldStoreNull() {
        UserResponse response = new UserResponse(1L, null, "John Doe");
        assertNull(response.getUsername());
    }

    @Test
    void userResponse_WithNullId_ShouldStoreNull() {
        UserResponse response = new UserResponse(null, "john@example.com", "John Doe");
        assertNull(response.getId());
    }

    // =========================
    // Stream Mapping Logic
    // (mirrors searchUsers() stream map to UserResponse)
    // =========================

    @Test
    void streamMapping_ShouldProduceCorrectNumberOfResponses() {
        User u1 = new User(); u1.setId(1L); u1.setUsername("alice@example.com"); u1.setName("Alice");
        User u2 = new User(); u2.setId(2L); u2.setUsername("albert@example.com"); u2.setName("Albert");
        User u3 = new User(); u3.setId(3L); u3.setUsername("alex@example.com"); u3.setName("Alex");

        List<User> users = List.of(u1, u2, u3);

        List<UserResponse> result = users.stream()
                .map(u -> new UserResponse(u.getId(), u.getUsername(), u.getName()))
                .toList();

        assertEquals(3, result.size());
    }

    @Test
    void streamMapping_ShouldMapAllFieldsCorrectly() {
        User u = new User();
        u.setId(5L);
        u.setUsername("bob@example.com");
        u.setName("Bob Smith");

        List<UserResponse> result = List.of(u).stream()
                .map(usr -> new UserResponse(usr.getId(), usr.getUsername(), usr.getName()))
                .toList();

        assertAll("Mapped UserResponse fields",
                () -> assertEquals(5L,                  result.get(0).getId()),
                () -> assertEquals("bob@example.com",   result.get(0).getUsername()),
                () -> assertEquals("Bob Smith",         result.get(0).getName())
        );
    }

    @Test
    void streamMapping_WithEmptyList_ShouldReturnEmptyList() {
        List<User> users = List.of();

        List<UserResponse> result = users.stream()
                .map(u -> new UserResponse(u.getId(), u.getUsername(), u.getName()))
                .toList();

        assertTrue(result.isEmpty());
    }

    @Test
    void streamMapping_ShouldPreserveOrderOfResults() {
        User u1 = new User(); u1.setId(1L); u1.setUsername("alpha@example.com"); u1.setName("Alpha");
        User u2 = new User(); u2.setId(2L); u2.setUsername("beta@example.com");  u2.setName("Beta");

        List<UserResponse> result = List.of(u1, u2).stream()
                .map(u -> new UserResponse(u.getId(), u.getUsername(), u.getName()))
                .toList();

        assertEquals("alpha@example.com", result.get(0).getUsername());
        assertEquals("beta@example.com",  result.get(1).getUsername());
    }

    // =========================
    // Search / Filter Logic
    // (mirrors findByUsernameContainingIgnoreCase behavior)
    // =========================

    @Test
    void searchLogic_CaseInsensitiveMatch_ShouldFindUser() {
        String query = "john";
        String username = "john@example.com";

        assertTrue(username.toLowerCase().contains(query.toLowerCase()),
                "Username should match query case-insensitively");
    }

    @Test
    void searchLogic_UpperCaseQuery_ShouldStillMatch() {
        String query = "JOHN";
        String username = "john@example.com";

        assertTrue(username.toLowerCase().contains(query.toLowerCase()),
                "Uppercase query should still match lowercase username");
    }

    @Test
    void searchLogic_PartialMatch_ShouldFindUser() {
        String query = "jo";
        String username = "john@example.com";

        assertTrue(username.toLowerCase().contains(query.toLowerCase()),
                "Partial query should match username");
    }

    @Test
    void searchLogic_NoMatch_ShouldReturnFalse() {
        String query = "xyz";
        String username = "john@example.com";

        assertFalse(username.toLowerCase().contains(query.toLowerCase()),
                "Non-matching query should return false");
    }

    @Test
    void searchLogic_EmptyQuery_ShouldMatchEverything() {
        String query = "";
        String username = "john@example.com";

        assertTrue(username.toLowerCase().contains(query.toLowerCase()),
                "Empty query should match any username");
    }

    @Test
    void searchLogic_FilterFromList_ShouldReturnOnlyMatches() {
        User u1 = new User(); u1.setUsername("alice@example.com");
        User u2 = new User(); u2.setUsername("albert@example.com");
        User u3 = new User(); u3.setUsername("bob@example.com");

        String query = "al";

        List<User> filtered = List.of(u1, u2, u3).stream()
                .filter(u -> u.getUsername().toLowerCase().contains(query.toLowerCase()))
                .toList();

        assertEquals(2, filtered.size(), "Only 'alice' and 'albert' should match 'al'");
    }
}