package com.muro.service;

import com.muro.entity.Trip;
import com.muro.entity.TripMember;
import com.muro.entity.User;
import com.muro.repository.TripMemberRepository;
import com.muro.repository.TripRepository;
import com.muro.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class TripMemberService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final TripMemberRepository tripMemberRepository;

    public TripMemberService(TripRepository tripRepository,
                             UserRepository userRepository,
                             TripMemberRepository tripMemberRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.tripMemberRepository = tripMemberRepository;
    }

    // ✅ ADD USER TO TRIP
    public void addUserToTrip(Long tripId, Long userId, Long currentUserId) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        // 🔐 OWNER CHECK
        if (!trip.getOwner().getId().equals(currentUserId)) {
            throw new RuntimeException("Not authorized");
        }

        // prevent duplicates
        if (tripMemberRepository.existsByTripIdAndUserId(tripId, userId)) {
            throw new RuntimeException("User already in trip");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TripMember member = new TripMember();
        member.setTrip(trip);
        member.setUser(user);
        member.setRole("MEMBER");

        tripMemberRepository.save(member);
    }

    // ❌ REMOVE USER FROM TRIP
    public void removeUserFromTrip(Long tripId, Long userId, Long currentUserId) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (!trip.getOwner().getId().equals(currentUserId)) {
            throw new RuntimeException("Not allowed. Only owner can remove members.");
        }

        TripMember member = tripMemberRepository
                .findByTripIdAndUserId(tripId, userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        tripMemberRepository.delete(member);
    }

}
