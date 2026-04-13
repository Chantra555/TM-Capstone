package com.muro.dto;

import com.muro.entity.Trip;

public class TripDTO {

    private Long id;
    private String name;
    private String location;
    private String startDate;
    private String endDate;
    private String idealBudget;

    public TripDTO(Trip trip) {
        this.id = trip.getId();
        this.name = trip.getName();
        this.location = trip.getLocation();
        this.startDate = String.valueOf(trip.getStartDate());
        this.endDate = String.valueOf(trip.getEndDate());
        this.idealBudget = String.valueOf(trip.getIdealBudget());
    }
}
