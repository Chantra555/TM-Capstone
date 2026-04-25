package com.muro.controller;

import com.muro.entity.Transportation;
import com.muro.repository.TransportationRepository;
import com.muro.repository.TripRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transportation")
public class TransportationController {

    private final TransportationRepository transportationRepo;
    private final TripRepository tripRepo;

    public TransportationController(TransportationRepository transportationRepo, TripRepository tripRepo) {
        this.transportationRepo = transportationRepo;
        this.tripRepo = tripRepo;
    }

    // ✅ GET all for a trip
    @GetMapping("/trip/{tripId}")
    public List<Transportation> getByTrip(@PathVariable Long tripId) {
        return transportationRepo.findByTripId(tripId);
    }

    // ✅ CREATE
    @PostMapping("/trip/{tripId}")
    public Transportation create(@PathVariable Long tripId, @RequestBody Transportation t) {
        t.setTrip(tripRepo.findById(tripId).orElseThrow());
        return transportationRepo.save(t);
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public Transportation update(@PathVariable Long id, @RequestBody Transportation updated) {
        Transportation t = transportationRepo.findById(id).orElseThrow();

        t.setType(updated.getType());
        t.setTitle(updated.getTitle());
        t.setDeparture(updated.getDeparture());
        t.setArrival(updated.getArrival());
        t.setDepartTime(updated.getDepartTime());
        t.setArriveTime(updated.getArriveTime());
        t.setCarrier(updated.getCarrier());
        t.setConfirmation(updated.getConfirmation());
        t.setNotes(updated.getNotes());

        return transportationRepo.save(t);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        transportationRepo.deleteById(id);
    }
}
