package com.muro.controller;

import com.muro.dto.TripMemberResponse;

import com.muro.dto.UserResponse;
import com.muro.entity.Trip;
import com.muro.entity.TripMember;
import com.muro.entity.User;
import com.muro.repository.TripMemberRepository;
import com.muro.repository.TripRepository;
import com.muro.repository.UserRepository;
import com.muro.service.TripMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripMemberController {

    private final TripMemberService tripMemberService;
    private final UserRepository userRepository;
    private final TripMemberRepository tripMemberRepository;
    private final TripRepository tripRepository;

    public TripMemberController(
            TripMemberService tripMemberService,
            UserRepository userRepository,
            TripMemberRepository tripMemberRepository,
            TripRepository tripRepository
    ) {
        this.tripMemberService = tripMemberService;
        this.userRepository = userRepository;
        this.tripMemberRepository = tripMemberRepository;
        this.tripRepository = tripRepository;
    }

    // ➕ ADD USER BY USERNAME
    @PostMapping("/{tripId}/members/by-username/{username}")
    public ResponseEntity<?> addUserByUsername(
            @PathVariable Long tripId,
            @PathVariable String username,
            Authentication auth
    ) {
        String currentUsername = auth.getName();

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User userToAdd = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        tripMemberService.addUserToTrip(
                tripId,
                userToAdd.getId(),
                currentUser.getId()
        );

        return ResponseEntity.ok("User added");
    }

    // ❌ REMOVE USER
    @DeleteMapping("/{tripId}/members/{userId}")
    public ResponseEntity<?> removeUser(
            @PathVariable Long tripId,
            @PathVariable Long userId,
            Authentication auth
    ) {
        String username = auth.getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        tripMemberService.removeUserFromTrip(
                tripId,
                userId,
                currentUser.getId()
        );

        return ResponseEntity.ok("User removed");
    }

    // 👥 GET OWNER + MEMBERS (THIS IS WHAT YOUR FRONTEND NEEDS)
    @GetMapping("/{tripId}/members")
    public ResponseEntity<TripMemberResponse> getMembers(@PathVariable Long tripId) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        List<TripMember> tripMembers = tripMemberRepository.findByTripId(tripId);

        List<UserResponse> members = tripMembers.stream()
                .map(m -> new UserResponse(
                        m.getUser().getId(),
                        m.getUser().getUsername()
                ))
                .toList();

        TripMemberResponse response = new TripMemberResponse(
                new UserResponse(
                        trip.getOwner().getId(),
                        trip.getOwner().getUsername()
                ),
                members
        );

        return ResponseEntity.ok(response);
    }

}
