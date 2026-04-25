package com.muro.dto;



public class UserResponse {

    private Long id;
    private String username;
    private String name;

    // two-arg constructor used in TripMemberController
    public UserResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    // three-arg constructor used elsewhere
    public UserResponse(Long id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }
}