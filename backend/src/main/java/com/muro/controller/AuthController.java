package com.muro.controller;

import com.muro.dto.LoginRequest;
import com.muro.dto.UserRequest;
import com.muro.dto.UserResponse;
import com.muro.entity.User;
import com.muro.JwtUtil;
import com.muro.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // =========================
    // SIGNUP
    // =========================
    @PostMapping("/signup")
    public UserResponse signup(@RequestBody UserRequest request) {

        try {
            User user = userService.signup(
                    request.getName(),
                    request.getUsername(),
                    request.getPassword()
            );

            return new UserResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getName()
            );

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        try {
            User user = userService.login(
                    request.getUsername(),
                    request.getPassword()
            );

            String token = jwtUtil.generateToken(user.getUsername());

            return new LoginResponse(
                    token,
                    user.getId(),
                    user.getUsername()
            );

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    e.getMessage()
            );
        }
    }

    // =========================
    // LOGIN RESPONSE
    // =========================
    public static class LoginResponse {

        private String token;
        private Long userId;
        private String username;

        public LoginResponse(String token, Long userId, String username) {
            this.token = token;
            this.userId = userId;
            this.username = username;
        }

        public String getToken() {
            return token;
        }

        public Long getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }
    }
}
