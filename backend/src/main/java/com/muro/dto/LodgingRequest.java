package com.muro.dto;

import java.time.LocalDate;

public class LodgingRequest {

    public String name;
    public String location;
    public LocalDate checkIn;
    public LocalDate checkOut;
    public Double price;
    public String notes;
    public Long tripId;
}
