package com.muro.controller;

import com.muro.dto.LoginRequest;
import com.muro.dto.UserRequest;
import com.muro.dto.UserResponse;
import com.muro.entity.User;
import com.muro.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173") // your frontend port
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Sign up a new user
     */
    @PostMapping("/signup")
    public UserResponse signup(@RequestBody UserRequest request) {
        try {
            User user = userService.signup(request.getName(), request.getUsername(), request.getPassword());
            return new UserResponse(user.getId(), user.getUsername(), user.getName());
        } catch (IllegalArgumentException e) {
            // For example, username already exists
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Login existing user
     */
    @PostMapping("/login")
    public UserResponse login(@RequestBody LoginRequest request) {
        try {
            User user = userService.login(request.getUsername(), request.getPassword());
            return new UserResponse(user.getId(), user.getUsername(), user.getName());
        } catch (IllegalArgumentException e) {
            // Wrong username/password
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
