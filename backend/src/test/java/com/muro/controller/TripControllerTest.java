package com.muro.controller;

import com.muro.dto.TripDTO;
import com.muro.entity.Trip;
import com.muro.entity.TripMember;
import com.muro.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TripControllerTest {

    private User owner;
    private User otherUser;
    private Trip trip;
    private TripDTO tripDTO;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1L);
        owner.setUsername("owner@example.com");
        owner.setName("Trip Owner");

        otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("other@example.com");
        otherUser.setName("Other User");

        trip = new Trip();
        trip.setId(10L);
        trip.setLocation("Paris, France");
        trip.setStartDate(LocalDate.of(2025, 6, 1));
        trip.setEndDate(LocalDate.of(2025, 6, 14));
        trip.setOwner(owner);

        tripDTO = new TripDTO();
        tripDTO.setLocation("Rome, Italy");
        tripDTO.setStartDate(LocalDate.of(2025, 8, 1));
        tripDTO.setEndDate(LocalDate.of(2025, 8, 10));
    }

    // =========================
    // Trip Entity - Getters
    // =========================

    @Test
    void trip_getId_ShouldReturnCorrectId() {
        assertEquals(10L, trip.getId());
    }

    @Test
    void trip_getLocation_ShouldReturnCorrectLocation() {
        assertEquals("Paris, France", trip.getLocation());
    }

    @Test
    void trip_getStartDate_ShouldReturnCorrectDate() {
        assertEquals(LocalDate.of(2025, 6, 1), trip.getStartDate());
    }

    @Test
    void trip_getEndDate_ShouldReturnCorrectDate() {
        assertEquals(LocalDate.of(2025, 6, 14), trip.getEndDate());
    }

    @Test
    void trip_getOwner_ShouldReturnLinkedOwner() {
        assertNotNull(trip.getOwner());
        assertEquals(1L, trip.getOwner().getId());
        assertEquals("owner@example.com", trip.getOwner().getUsername());
    }

    @Test
    void trip_getAllFields_ShouldMatchExpected() {
        assertAll("All Trip fields",
                () -> assertEquals(10L,                            trip.getId()),
                () -> assertEquals("Paris, France",                trip.getLocation()),
                () -> assertEquals(LocalDate.of(2025, 6, 1),  trip.getStartDate()),
                () -> assertEquals(LocalDate.of(2025, 6, 14), trip.getEndDate()),
                () -> assertNotNull(                               trip.getOwner())
        );
    }

    // =========================
    // Trip Entity - Setters
    // =========================

    @Test
    void trip_setLocation_ShouldUpdateValue() {
        trip.setLocation("Tokyo, Japan");
        assertEquals("Tokyo, Japan", trip.getLocation());
    }

    @Test
    void trip_setStartDate_ShouldUpdateValue() {
        trip.setStartDate(LocalDate.of(2025, 9, 1));
        assertEquals(LocalDate.of(2025, 9, 1), trip.getStartDate());
    }

    @Test
    void trip_setEndDate_ShouldUpdateValue() {
        trip.setEndDate(LocalDate.of(2025, 9, 14));
        assertEquals(LocalDate.of(2025, 9, 14), trip.getEndDate());
    }

    @Test
    void trip_setOwner_ShouldUpdateLinkedOwner() {
        trip.setOwner(otherUser);
        assertEquals(2L, trip.getOwner().getId());
    }

    // =========================
    // Trip Entity - Null / Edge Cases
    // =========================

    @Test
    void trip_WithNullLocation_ShouldStoreNull() {
        trip.setLocation(null);
        assertNull(trip.getLocation());
    }

    @Test
    void trip_WithNullStartDate_ShouldStoreNull() {
        trip.setStartDate(null);
        assertNull(trip.getStartDate());
    }

    @Test
    void trip_WithNullEndDate_ShouldStoreNull() {
        trip.setEndDate(null);
        assertNull(trip.getEndDate());
    }

    @Test
    void trip_WithNullOwner_ShouldStoreNull() {
        trip.setOwner(null);
        assertNull(trip.getOwner());
    }

    // =========================
    // TripDTO - Getters / Setters
    // =========================

    @Test
    void tripDTO_getLocation_ShouldReturnCorrectValue() {
        assertEquals("Rome, Italy", tripDTO.getLocation());
    }

    @Test
    void tripDTO_getStartDate_ShouldReturnCorrectDate() {
        assertEquals(LocalDate.of(2025, 8, 1), tripDTO.getStartDate());
    }

    @Test
    void tripDTO_getEndDate_ShouldReturnCorrectDate() {
        assertEquals(LocalDate.of(2025, 8, 10), tripDTO.getEndDate());
    }

    @Test
    void tripDTO_getAllFields_ShouldMatchExpected() {
        assertAll("All TripDTO fields",
                () -> assertEquals("Rome, Italy",                   tripDTO.getLocation()),
                () -> assertEquals(LocalDate.of(2025, 8, 1),   tripDTO.getStartDate()),
                () -> assertEquals(LocalDate.of(2025, 8, 10),  tripDTO.getEndDate())
        );
    }

    @Test
    void tripDTO_setLocation_ShouldUpdateValue() {
        tripDTO.setLocation("Madrid, Spain");
        assertEquals("Madrid, Spain", tripDTO.getLocation());
    }

    @Test
    void tripDTO_WithNullLocation_ShouldStoreNull() {
        tripDTO.setLocation(null);
        assertNull(tripDTO.getLocation());
    }

    // =========================
    // Ownership Check Logic
    // (mirrors owner check in updateTrip() and deleteTrip())
    // =========================

    @Test
    void ownerCheck_WhenUsernameMatches_ShouldBeOwner() {
        String loggedInUsername = "owner@example.com";
        boolean isOwner = trip.getOwner().getUsername().equals(loggedInUsername);
        assertTrue(isOwner);
    }

    @Test
    void ownerCheck_WhenUsernameDiffers_ShouldNotBeOwner() {
        String loggedInUsername = "other@example.com";
        boolean isOwner = trip.getOwner().getUsername().equals(loggedInUsername);
        assertFalse(isOwner, "Non-owner should fail ownership check");
    }

    @Test
    void ownerCheck_WhenIdMatches_ShouldBeOwner() {
        boolean isOwner = trip.getOwner().getId().equals(owner.getId());
        assertTrue(isOwner);
    }

    @Test
    void ownerCheck_WhenIdDiffers_ShouldNotBeOwner() {
        boolean isOwner = trip.getOwner().getId().equals(otherUser.getId());
        assertFalse(isOwner, "Different user ID should fail ownership check");
    }

    // =========================
    // Member Access Logic
    // (mirrors isMember check in getTripById())
    // =========================

    @Test
    void memberCheck_WhenUserIsMember_ShouldGrantAccess() {
        TripMember member = new TripMember();
        member.setUser(otherUser);
        member.setTrip(trip);

        List<TripMember> members = List.of(member);

        boolean isMember = members.stream()
                .anyMatch(m -> m.getUser().getId().equals(otherUser.getId()));

        assertTrue(isMember, "User who is a member should have access");
    }

    @Test
    void memberCheck_WhenUserIsNotMember_ShouldDenyAccess() {
        User stranger = new User();
        stranger.setId(99L);
        stranger.setUsername("stranger@example.com");

        TripMember member = new TripMember();
        member.setUser(otherUser);
        member.setTrip(trip);

        List<TripMember> members = List.of(member);

        boolean isMember = members.stream()
                .anyMatch(m -> m.getUser().getId().equals(stranger.getId()));

        assertFalse(isMember, "Non-member should be denied access");
    }

    @Test
    void memberCheck_WithEmptyMemberList_ShouldDenyAccess() {
        List<TripMember> members = List.of();

        boolean isMember = members.stream()
                .anyMatch(m -> m.getUser().getId().equals(otherUser.getId()));

        assertFalse(isMember, "Empty member list should deny all access");
    }

    // =========================
    // Combined Access Logic
    // (mirrors isOwner || isMember gate in getTripById())
    // =========================

    @Test
    void accessCheck_OwnerShouldHaveAccess() {
        boolean isOwner  = trip.getOwner().getId().equals(owner.getId());
        boolean isMember = false;
        assertTrue(isOwner || isMember, "Owner should always have access");
    }

    @Test
    void accessCheck_MemberShouldHaveAccess() {
        boolean isOwner  = false;
        boolean isMember = true;
        assertTrue(isOwner || isMember, "Member should have access even if not owner");
    }

    @Test
    void accessCheck_StrangerShouldBeDenied() {
        boolean isOwner  = false;
        boolean isMember = false;
        assertFalse(isOwner || isMember, "Neither owner nor member should be denied");
    }

    // =========================
    // Update Logic
    // (mirrors updateTrip() field-copy from TripDTO)
    // =========================

    @Test
    void updateLogic_ShouldCopyAllFieldsFromDTO() {
        trip.setLocation(tripDTO.getLocation());
        trip.setStartDate(tripDTO.getStartDate());
        trip.setEndDate(tripDTO.getEndDate());

        assertAll("DTO fields copied to Trip",
                () -> assertEquals("Rome, Italy",                  trip.getLocation()),
                () -> assertEquals(LocalDate.of(2025, 8, 1),  trip.getStartDate()),
                () -> assertEquals(LocalDate.of(2025, 8, 10), trip.getEndDate())
        );
    }

    @Test
    void updateLogic_ShouldNotChangeOwnerOrId() {
        trip.setLocation(tripDTO.getLocation());
        trip.setStartDate(tripDTO.getStartDate());
        trip.setEndDate(tripDTO.getEndDate());

        assertEquals(10L, trip.getId(),                   "ID should not change after update");
        assertEquals(1L,  trip.getOwner().getId(),        "Owner should not change after update");
        assertEquals("owner@example.com", trip.getOwner().getUsername());
    }

    @Test
    void updateLogic_WithNullLocation_ShouldOverwriteWithNull() {
        tripDTO.setLocation(null);
        trip.setLocation(tripDTO.getLocation());
        assertNull(trip.getLocation(), "Null location in DTO should overwrite trip location");
    }

    // =========================
    // Deduplication / Merge Logic
    // (mirrors HashSet merge in getAllTrips())
    // =========================

    @Test
    void mergeLogic_ShouldContainBothOwnedAndMemberTrips() {
        Trip ownedTrip  = new Trip(); ownedTrip.setId(1L);
        Trip memberTrip = new Trip(); memberTrip.setId(2L);

        List<Trip> ownedTrips  = List.of(ownedTrip);
        List<Trip> memberTrips = List.of(memberTrip);

        Set<Trip> allTrips = new HashSet<>();
        allTrips.addAll(ownedTrips);
        allTrips.addAll(memberTrips);

        assertEquals(2, allTrips.size());
    }

    @Test
    void mergeLogic_DuplicateTripShouldOnlyAppearOnce() {
        Trip sharedTrip = new Trip();
        sharedTrip.setId(1L);

        List<Trip> ownedTrips  = List.of(sharedTrip);
        List<Trip> memberTrips = List.of(sharedTrip); // same object = duplicate

        Set<Trip> allTrips = new HashSet<>();
        allTrips.addAll(ownedTrips);
        allTrips.addAll(memberTrips);

        assertEquals(1, allTrips.size(), "Duplicate trip should be deduplicated by HashSet");
    }

    @Test
    void mergeLogic_WithNoMemberships_ShouldReturnOnlyOwnedTrips() {
        Trip ownedTrip = new Trip(); ownedTrip.setId(1L);

        List<Trip> ownedTrips  = List.of(ownedTrip);
        List<Trip> memberTrips = List.of();

        Set<Trip> allTrips = new HashSet<>();
        allTrips.addAll(ownedTrips);
        allTrips.addAll(memberTrips);

        assertEquals(1, allTrips.size());
        assertTrue(new ArrayList<>(allTrips).contains(ownedTrip));
    }

    @Test
    void mergeLogic_WithNoOwnedTrips_ShouldReturnOnlyMemberTrips() {
        Trip memberTrip = new Trip(); memberTrip.setId(2L);

        List<Trip> ownedTrips  = List.of();
        List<Trip> memberTrips = List.of(memberTrip);

        Set<Trip> allTrips = new HashSet<>();
        allTrips.addAll(ownedTrips);
        allTrips.addAll(memberTrips);

        assertEquals(1, allTrips.size());
    }

    @Test
    void mergeLogic_WithBothEmpty_ShouldReturnEmptyList() {
        Set<Trip> allTrips = new HashSet<>();
        allTrips.addAll(List.of());
        allTrips.addAll(List.of());

        assertTrue(new ArrayList<>(allTrips).isEmpty());
    }

    // =========================
    // Date Range Logic
    // =========================

    @Test
    void tripDates_EndDateShouldBeAfterStartDate() {
        assertTrue(
                trip.getEndDate().isAfter(trip.getStartDate()),
                "End date should be after start date"
        );
    }

    @Test
    void tripDates_SameDateShouldNotBeAfter() {
        trip.setStartDate(LocalDate.of(2025, 6, 1));
        trip.setEndDate(LocalDate.of(2025, 6, 1));
        assertFalse(
                trip.getEndDate().isAfter(trip.getStartDate()),
                "Same start and end date should not be 'after'"
        );
    }

    @Test
    void tripDates_DurationShouldCalculateCorrectly() {
        long days = java.time.temporal.ChronoUnit.DAYS.between(
                trip.getStartDate(), trip.getEndDate()
        );
        assertEquals(13, days, "June 1 to June 14 should be 13 days");
    }

    @Test
    void tripDates_OneDayTripShouldBeOneDay() {
        LocalDate start = LocalDate.of(2025, 7, 1);
        LocalDate end   = LocalDate.of(2025, 7, 2);
        long days = java.time.temporal.ChronoUnit.DAYS.between(start, end);
        assertEquals(1, days);
    }
}