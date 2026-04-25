package com.muro.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "trip_members",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"trip_id", "user_id"})
        }
)
public class TripMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 👇 Many users can belong to many trips (via this join entity)
    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 👇 Optional role system (future-proof)
    private String role; // "OWNER", "ADMIN", "MEMBER"

    // ------------------------
    // Constructors
    // ------------------------

    public TripMember() {}

    public TripMember(Trip trip, User user, String role) {
        this.trip = trip;
        this.user = user;
        this.role = role;
    }

    // ------------------------
    // Getters / Setters
    // ------------------------

    public Long getId() {
        return id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
