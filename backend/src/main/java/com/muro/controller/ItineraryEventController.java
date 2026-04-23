package com.muro.controller;

import com.muro.entity.ItineraryEvent;
import com.muro.entity.Trip;
import com.muro.repository.ItineraryEventRepository;
import com.muro.repository.TripRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itinerary")
@CrossOrigin(origins = "http://localhost:5173")
public class ItineraryEventController {

    private final ItineraryEventRepository itineraryRepo;
    private final TripRepository tripRepo;

    public ItineraryEventController(ItineraryEventRepository itineraryRepo, TripRepository tripRepo) {
        this.itineraryRepo = itineraryRepo;
        this.tripRepo = tripRepo;
    }

    @GetMapping("/trip/{tripId}")
    public List<ItineraryEvent> getByTrip(@PathVariable Long tripId) {
        return itineraryRepo.findByTripId(tripId);
    }

    @PostMapping("/trip/{tripId}")
    public ItineraryEvent create(
            @PathVariable Long tripId,
            @RequestBody ItineraryEvent event
    ) {
        Trip trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        event.setTrip(trip);

        return itineraryRepo.save(event);
    }
    @PutMapping("/{id}")
    public ItineraryEvent updateEvent(
            @PathVariable Long id,
            @RequestBody ItineraryEvent updatedEvent
    ) {
        ItineraryEvent existing = itineraryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        existing.setTitle(updatedEvent.getTitle());
        existing.setEventTime(updatedEvent.getEventTime());
        existing.setEventDate(updatedEvent.getEventDate());
        existing.setType(updatedEvent.getType());
        existing.setDescription(updatedEvent.getDescription());

        return itineraryRepo.save(existing);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (!itineraryRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        itineraryRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
