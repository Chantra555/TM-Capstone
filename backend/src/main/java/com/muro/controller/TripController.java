package com.muro.controller;

import com.muro.dto.TripDTO;
import com.muro.dto.TripResponse;
import com.muro.dto.UserResponse;
import com.muro.entity.Trip;
import com.muro.entity.TripMember;
import com.muro.entity.User;
import com.muro.repository.TripMemberRepository;
import com.muro.repository.TripRepository;
import com.muro.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = "http://localhost:5173")
public class TripController {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final TripMemberRepository tripMemberRepository;

    public TripController(
            TripRepository tripRepository,
            UserRepository userRepository,
            TripMemberRepository tripMemberRepository
    ) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.tripMemberRepository = tripMemberRepository;
    }


    // --------------------------------------------------
    // CREATE TRIP (sets OWNER)
    // --------------------------------------------------
    @PostMapping
    public ResponseEntity<?> createTrip(@RequestBody Trip trip,
                                        Principal principal) {

        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 👑 OWNER assignment (IMPORTANT CHANGE)
        trip.setOwner(user);

        Trip saved = tripRepository.save(trip);

        return ResponseEntity.ok(saved);
    }

    // --------------------------------------------------
    // GET ALL TRIPS (owned by user)
    // --------------------------------------------------
    @GetMapping
    public List<Trip> getAllTrips(Principal principal) {

        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 👑 trips you own
        List<Trip> ownedTrips = tripRepository.findByOwner(user);

        // 👥 trips you are a member of
        List<TripMember> memberships = tripMemberRepository.findByUser(user);

        List<Trip> memberTrips = memberships.stream()
                .map(TripMember::getTrip)
                .toList();

        // 🔥 merge + remove duplicates
        Set<Trip> allTrips = new HashSet<>();
        allTrips.addAll(ownedTrips);
        allTrips.addAll(memberTrips);

        return new ArrayList<>(allTrips);
    }




    // --------------------------------------------------
    // GET SINGLE TRIP (owner only for now) -- changed 2 trip if in
    // --------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getTripById(
            @PathVariable Long id,
            Principal principal
    ) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        boolean isOwner = trip.getOwner().getId().equals(user.getId());

        boolean isMember = tripMemberRepository
                .findByTripId(id)
                .stream()
                .anyMatch(m -> m.getUser().getId().equals(user.getId()));

        if (!isOwner && !isMember) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(trip);
    }


    // --------------------------------------------------
    // UPDATE TRIP (owner only)
    // --------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTrip(@PathVariable Long id,
                                        @RequestBody TripDTO dto,
                                        Principal principal) {

        String username = principal.getName();

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        // 🔐 OWNER CHECK
        if (!trip.getOwner().getUsername().equals(username)) {
            return ResponseEntity.status(403).build();
        }

        trip.setLocation(dto.getLocation());
        trip.setStartDate(dto.getStartDate());
        trip.setEndDate(dto.getEndDate());

        return ResponseEntity.ok(tripRepository.save(trip));
    }

    // --------------------------------------------------
    // DELETE TRIP (owner only)
    // --------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrip(@PathVariable Long id,
                                        @AuthenticationPrincipal UserDetails userDetails) {

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (!trip.getOwner().getUsername().equals(userDetails.getUsername())) {
            return ResponseEntity.status(403).build();
        }

        tripRepository.delete(trip);

        return ResponseEntity.ok().build();
    }
}
