package com.muro.controller;

import com.muro.dto.TripMemberResponse;
import com.muro.dto.UserResponse;
import com.muro.entity.Trip;
import com.muro.entity.TripMember;
import com.muro.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TripMemberControllerTest {

    private User owner;
    private User memberUser;
    private Trip trip;
    private TripMember tripMember;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1L);
        owner.setUsername("owner@example.com");
        owner.setName("Trip Owner");

        memberUser = new User();
        memberUser.setId(2L);
        memberUser.setUsername("member@example.com");
        memberUser.setName("Trip Member");

        trip = new Trip();
        trip.setId(10L);
        trip.setOwner(owner);

        tripMember = new TripMember();
        tripMember.setUser(memberUser);
        tripMember.setTrip(trip);
    }

    // =========================
    // TripMember Entity - Getters
    // =========================

    @Test
    void tripMember_getUser_ShouldReturnLinkedUser() {
        assertNotNull(tripMember.getUser());
        assertEquals(2L, tripMember.getUser().getId());
    }

    @Test
    void tripMember_getTrip_ShouldReturnLinkedTrip() {
        assertNotNull(tripMember.getTrip());
        assertEquals(10L, tripMember.getTrip().getId());
    }

    @Test
    void tripMember_getUserUsername_ShouldReturnCorrectUsername() {
        assertEquals("member@example.com", tripMember.getUser().getUsername());
    }

    @Test
    void tripMember_getAllFields_ShouldMatchExpected() {
        assertAll("All TripMember fields",
                () -> assertNotNull(tripMember.getUser()),
                () -> assertNotNull(tripMember.getTrip()),
                () -> assertEquals(2L,                      tripMember.getUser().getId()),
                () -> assertEquals("member@example.com",    tripMember.getUser().getUsername()),
                () -> assertEquals(10L,                     tripMember.getTrip().getId())
        );
    }

    // =========================
    // TripMember Entity - Setters
    // =========================

    @Test
    void tripMember_setUser_ShouldUpdateLinkedUser() {
        User newUser = new User();
        newUser.setId(99L);
        newUser.setUsername("new@example.com");

        tripMember.setUser(newUser);

        assertEquals(99L, tripMember.getUser().getId());
        assertEquals("new@example.com", tripMember.getUser().getUsername());
    }

    @Test
    void tripMember_setTrip_ShouldUpdateLinkedTrip() {
        Trip newTrip = new Trip();
        newTrip.setId(55L);

        tripMember.setTrip(newTrip);

        assertEquals(55L, tripMember.getTrip().getId());
    }

    // =========================
    // TripMember Entity - Null / Edge Cases
    // =========================

    @Test
    void tripMember_WithNullUser_ShouldStoreNull() {
        tripMember.setUser(null);
        assertNull(tripMember.getUser());
    }

    @Test
    void tripMember_WithNullTrip_ShouldStoreNull() {
        tripMember.setTrip(null);
        assertNull(tripMember.getTrip());
    }

    // =========================
    // UserResponse DTO - Getters
    // =========================

    @Test
    void userResponse_getId_ShouldReturnCorrectId() {
        UserResponse response = new UserResponse(1L, "owner@example.com");
        assertEquals(1L, response.getId());
    }

    @Test
    void userResponse_getUsername_ShouldReturnCorrectUsername() {
        UserResponse response = new UserResponse(1L, "owner@example.com");
        assertEquals("owner@example.com", response.getUsername());
    }

    @Test
    void userResponse_getAllFields_ShouldMatchExpected() {
        UserResponse response = new UserResponse(2L, "member@example.com");
        assertAll("All UserResponse fields",
                () -> assertEquals(2L,                      response.getId()),
                () -> assertEquals("member@example.com",    response.getUsername())
        );
    }

    // =========================
    // UserResponse DTO - Null / Edge Cases
    // =========================

    @Test
    void userResponse_WithNullId_ShouldStoreNull() {
        UserResponse response = new UserResponse(null, "member@example.com");
        assertNull(response.getId());
    }

    @Test
    void userResponse_WithNullUsername_ShouldStoreNull() {
        UserResponse response = new UserResponse(1L, null);
        assertNull(response.getUsername());
    }

    // =========================
    // TripMemberResponse DTO
    // =========================

    @Test
    void tripMemberResponse_getOwner_ShouldReturnCorrectOwner() {
        UserResponse ownerResponse   = new UserResponse(1L, "owner@example.com");
        UserResponse memberResponse  = new UserResponse(2L, "member@example.com");
        TripMemberResponse response  = new TripMemberResponse(ownerResponse, List.of(memberResponse));

        assertNotNull(response.getOwner());
        assertEquals(1L,                    response.getOwner().getId());
        assertEquals("owner@example.com",   response.getOwner().getUsername());
    }

    @Test
    void tripMemberResponse_getMembers_ShouldReturnCorrectList() {
        UserResponse ownerResponse  = new UserResponse(1L, "owner@example.com");
        UserResponse memberResponse = new UserResponse(2L, "member@example.com");
        TripMemberResponse response = new TripMemberResponse(ownerResponse, List.of(memberResponse));

        assertEquals(1, response.getMembers().size());
        assertEquals("member@example.com", response.getMembers().get(0).getUsername());
    }

    @Test
    void tripMemberResponse_WithEmptyMembers_ShouldReturnEmptyList() {
        UserResponse ownerResponse  = new UserResponse(1L, "owner@example.com");
        TripMemberResponse response = new TripMemberResponse(ownerResponse, List.of());

        assertTrue(response.getMembers().isEmpty());
    }

    @Test
    void tripMemberResponse_WithNullOwner_ShouldStoreNull() {
        TripMemberResponse response = new TripMemberResponse(null, List.of());
        assertNull(response.getOwner());
    }

    @Test
    void tripMemberResponse_getAllFields_ShouldMatchExpected() {
        UserResponse ownerResponse   = new UserResponse(1L, "owner@example.com");
        UserResponse member1         = new UserResponse(2L, "member@example.com");
        UserResponse member2         = new UserResponse(3L, "another@example.com");
        TripMemberResponse response  = new TripMemberResponse(ownerResponse, List.of(member1, member2));

        assertAll("All TripMemberResponse fields",
                () -> assertEquals(1L,                    response.getOwner().getId()),
                () -> assertEquals("owner@example.com",   response.getOwner().getUsername()),
                () -> assertEquals(2,                     response.getMembers().size())
        );
    }

    // =========================
    // Stream Mapping Logic
    // (mirrors getMembers() stream map from TripMember → UserResponse)
    // =========================

    @Test
    void streamMapping_ShouldProduceCorrectNumberOfResponses() {
        User u1 = new User(); u1.setId(2L); u1.setUsername("member1@example.com");
        User u2 = new User(); u2.setId(3L); u2.setUsername("member2@example.com");

        TripMember m1 = new TripMember(); m1.setUser(u1);
        TripMember m2 = new TripMember(); m2.setUser(u2);

        List<TripMember> tripMembers = List.of(m1, m2);

        List<UserResponse> result = tripMembers.stream()
                .map(m -> new UserResponse(m.getUser().getId(), m.getUser().getUsername()))
                .toList();

        assertEquals(2, result.size());
    }

    @Test
    void streamMapping_ShouldMapUserIdAndUsernameCorrectly() {
        User u = new User();
        u.setId(5L);
        u.setUsername("alice@example.com");

        TripMember m = new TripMember();
        m.setUser(u);

        List<UserResponse> result = List.of(m).stream()
                .map(tm -> new UserResponse(tm.getUser().getId(), tm.getUser().getUsername()))
                .toList();

        assertAll("Mapped UserResponse from TripMember",
                () -> assertEquals(5L,                      result.get(0).getId()),
                () -> assertEquals("alice@example.com",     result.get(0).getUsername())
        );
    }

    @Test
    void streamMapping_WithEmptyMemberList_ShouldReturnEmptyList() {
        List<TripMember> tripMembers = List.of();

        List<UserResponse> result = tripMembers.stream()
                .map(m -> new UserResponse(m.getUser().getId(), m.getUser().getUsername()))
                .toList();

        assertTrue(result.isEmpty());
    }

    @Test
    void streamMapping_ShouldPreserveOrder() {
        User u1 = new User(); u1.setId(1L); u1.setUsername("first@example.com");
        User u2 = new User(); u2.setId(2L); u2.setUsername("second@example.com");
        User u3 = new User(); u3.setId(3L); u3.setUsername("third@example.com");

        TripMember m1 = new TripMember(); m1.setUser(u1);
        TripMember m2 = new TripMember(); m2.setUser(u2);
        TripMember m3 = new TripMember(); m3.setUser(u3);

        List<UserResponse> result = List.of(m1, m2, m3).stream()
                .map(m -> new UserResponse(m.getUser().getId(), m.getUser().getUsername()))
                .toList();

        assertEquals("first@example.com",  result.get(0).getUsername());
        assertEquals("second@example.com", result.get(1).getUsername());
        assertEquals("third@example.com",  result.get(2).getUsername());
    }

    // =========================
    // TripMemberResponse Build Logic
    // (mirrors full getMembers() response assembly)
    // =========================

    @Test
    void buildResponse_OwnerShouldBePopulatedFromTrip() {
        UserResponse ownerResponse = new UserResponse(
                trip.getOwner().getId(),
                trip.getOwner().getUsername()
        );

        assertEquals(1L,                    ownerResponse.getId());
        assertEquals("owner@example.com",   ownerResponse.getUsername());
    }

    @Test
    void buildResponse_MembersShouldBePopulatedFromTripMembers() {
        List<TripMember> tripMembers = List.of(tripMember);

        List<UserResponse> members = tripMembers.stream()
                .map(m -> new UserResponse(m.getUser().getId(), m.getUser().getUsername()))
                .toList();

        TripMemberResponse response = new TripMemberResponse(
                new UserResponse(trip.getOwner().getId(), trip.getOwner().getUsername()),
                members
        );

        assertAll("Full response assembly",
                () -> assertEquals(1L,                  response.getOwner().getId()),
                () -> assertEquals("owner@example.com", response.getOwner().getUsername()),
                () -> assertEquals(1,                   response.getMembers().size()),
                () -> assertEquals("member@example.com",response.getMembers().get(0).getUsername())
        );
    }

    @Test
    void buildResponse_WithMultipleMembers_ShouldContainAll() {
        User u2 = new User(); u2.setId(3L); u2.setUsername("second@example.com");
        TripMember m2 = new TripMember(); m2.setUser(u2);

        List<TripMember> tripMembers = List.of(tripMember, m2);

        List<UserResponse> members = tripMembers.stream()
                .map(m -> new UserResponse(m.getUser().getId(), m.getUser().getUsername()))
                .toList();

        TripMemberResponse response = new TripMemberResponse(
                new UserResponse(trip.getOwner().getId(), trip.getOwner().getUsername()),
                members
        );

        assertEquals(2, response.getMembers().size());
        assertEquals("member@example.com",  response.getMembers().get(0).getUsername());
        assertEquals("second@example.com",  response.getMembers().get(1).getUsername());
    }
}