package com.muro.controller;

import com.muro.dto.UserResponse;
import com.muro.entity.User;
import com.muro.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 🔍 SEARCH USERS (for invite system)
    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam String q) {

        List<User> users = userRepository
                .findByUsernameContainingIgnoreCase(q);

        List<UserResponse> result = users.stream()
                .map(u -> new UserResponse(
                        u.getId(),
                        u.getUsername(),
                        u.getName()
                ))
                .toList();

        return ResponseEntity.ok(result);
    }
}
