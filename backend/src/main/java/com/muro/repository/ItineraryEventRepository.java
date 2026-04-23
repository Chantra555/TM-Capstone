package com.muro.repository;

import com.muro.entity.ItineraryEvent;
import com.muro.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItineraryEventRepository extends JpaRepository<ItineraryEvent, Long> {

    List<ItineraryEvent> findByTrip(Trip trip);

    List<ItineraryEvent> findByTripId(Long tripId);
}
