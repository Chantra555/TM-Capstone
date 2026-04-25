package com.muro.dto;


import java.util.List;

public class TripMemberResponse {

    private UserResponse owner;
    private List<UserResponse> members;

    public TripMemberResponse(UserResponse owner, List<UserResponse> members) {
        this.owner = owner;
        this.members = members;
    }

    public UserResponse getOwner() {
        return owner;
    }

    public List<UserResponse> getMembers() {
        return members;
    }
}
