package com.muro.dto;

import com.muro.entity.Trip;
import java.time.LocalDate;

public class TripDTO {

    private Long id;
    private String name;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double idealBudget;

    // ✅ REQUIRED for Spring
    public TripDTO() {}

    // existing mapping constructor (for GET responses)
    public TripDTO(Trip trip) {
        this.id = trip.getId();
        this.name = trip.getName();
        this.location = trip.getLocation();
        this.startDate = trip.getStartDate();
        this.endDate = trip.getEndDate();
        this.idealBudget = trip.getIdealBudget();
    }

    // ✅ getters + setters (REQUIRED)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getIdealBudget() {
        return idealBudget;
    }

    public void setIdealBudget(Double idealBudget) {
        this.idealBudget = idealBudget;
    }
}
