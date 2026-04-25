package com.muro.dto;

import java.time.LocalDate;
import java.util.List;

public class TripResponse {

    private Long id;
    private String name;
    private String location;
    private Double idealBudget;
    private LocalDate startDate;
    private LocalDate endDate;

    // 👑 owner info
    private UserResponse owner;

    // 👥 members (optional but very useful)
    private List<UserResponse> members;

    public TripResponse(Long id,
                        String name,
                        String location,
                        Double idealBudget,
                        LocalDate startDate,
                        LocalDate endDate,
                        UserResponse owner,
                        List<UserResponse> members) {

        this.id = id;
        this.name = name;
        this.location = location;
        this.idealBudget = idealBudget;
        this.startDate = startDate;
        this.endDate = endDate;
        this.owner = owner;
        this.members = members;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public Double getIdealBudget() { return idealBudget; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public UserResponse getOwner() { return owner; }
    public List<UserResponse> getMembers() { return members; }
}
