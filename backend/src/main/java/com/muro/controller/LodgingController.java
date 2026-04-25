package com.muro.controller;

import com.muro.dto.LodgingRequest;
import com.muro.entity.Lodging;
import com.muro.entity.Trip;
import com.muro.repository.LodgingRepository;
import com.muro.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lodgings")
@CrossOrigin
public class LodgingController {

    @Autowired
    private LodgingRepository lodgingRepository;

    @Autowired
    private TripRepository tripRepository;

    // CREATE lodging
    @PostMapping
    public ResponseEntity<Lodging> createLodging(@RequestBody LodgingRequest req) {

        Trip trip = tripRepository.findById(req.tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        Lodging lodging = new Lodging();
        lodging.setName(req.name);
        lodging.setLocation(req.location);
        lodging.setCheckIn(req.checkIn);
        lodging.setCheckOut(req.checkOut);
        lodging.setPrice(req.price);
        lodging.setNotes(req.notes);
        lodging.setTrip(trip);

        return ResponseEntity.ok(lodgingRepository.save(lodging));
    }

    // GET all lodgings for a trip
    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<Lodging>> getByTrip(@PathVariable Long tripId) {
        return ResponseEntity.ok(lodgingRepository.findByTripId(tripId));
    }

    // DELETE lodging
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLodging(@PathVariable Long id) {
        lodgingRepository.deleteById(id);
        return ResponseEntity.ok("Deleted");
    }
}
