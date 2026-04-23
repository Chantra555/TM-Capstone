package com.muro.controller;

import com.muro.dto.TripDTO;
import com.muro.entity.Trip;
import com.muro.entity.User;
import com.muro.repository.TripRepository;
import com.muro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = "http://localhost:5173")

public class TripController {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<String> createTrip(@RequestBody Trip trip, Principal principal) {

        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        trip.setUser(user);

        Trip saved = tripRepository.save(trip);

        // 🔥 ADD THIS LINE HERE
        System.out.println("SAVED TRIP ID = " + saved.getId());

        return ResponseEntity.ok("Trip created successfully");
    }
    @GetMapping
    public List<Trip> getAllTrips(Principal principal) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return tripRepository.findByUser(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTripById(@PathVariable Long id, Principal principal) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return tripRepository.findByIdAndUser(id, user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrip(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (!trip.getUser().getUsername().equals(userDetails.getUsername())) {
            return ResponseEntity.status(403).build();
        }

        tripRepository.delete(trip);
        return ResponseEntity.ok().build();
    }


}
