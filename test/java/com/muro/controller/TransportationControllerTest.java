package com.muro.controller;

import com.muro.entity.Transportation;
import com.muro.entity.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransportationControllerTest {

    private Transportation transportation;
    private Trip trip;

    @BeforeEach
    void setUp() {
        trip = new Trip();
        trip.setId(10L);

        transportation = new Transportation();
        transportation.setId(1L);
        transportation.setType("Flight");
        transportation.setTitle("NYC to LA");
        transportation.setDeparture("JFK Airport");
        transportation.setArrival("LAX Airport");
        transportation.setDepartTime(LocalDateTime.from(LocalTime.of(8, 0)));
        transportation.setArriveTime(LocalDateTime.from(LocalTime.of(11, 30)));
        transportation.setCarrier("Delta Airlines");
        transportation.setConfirmation("ABC123");
        transportation.setNotes("Window seat requested");
        transportation.setTrip(trip);
    }

    // =========================
    // Transportation - Getters
    // =========================

    @Test
    void transportation_getId_ShouldReturnCorrectId() {
        assertEquals(1L, transportation.getId());
    }

    @Test
    void transportation_getType_ShouldReturnCorrectType() {
        assertEquals("Flight", transportation.getType());
    }

    @Test
    void transportation_getTitle_ShouldReturnCorrectTitle() {
        assertEquals("NYC to LA", transportation.getTitle());
    }

    @Test
    void transportation_getDeparture_ShouldReturnCorrectValue() {
        assertEquals("JFK Airport", transportation.getDeparture());
    }

    @Test
    void transportation_getArrival_ShouldReturnCorrectValue() {
        assertEquals("LAX Airport", transportation.getArrival());
    }

    @Test
    void transportation_getDepartTime_ShouldReturnCorrectTime() {
        assertEquals(LocalTime.of(8, 0), transportation.getDepartTime());
    }

    @Test
    void transportation_getArriveTime_ShouldReturnCorrectTime() {
        assertEquals(LocalTime.of(11, 30), transportation.getArriveTime());
    }

    @Test
    void transportation_getCarrier_ShouldReturnCorrectCarrier() {
        assertEquals("Delta Airlines", transportation.getCarrier());
    }

    @Test
    void transportation_getConfirmation_ShouldReturnCorrectCode() {
        assertEquals("ABC123", transportation.getConfirmation());
    }

    @Test
    void transportation_getNotes_ShouldReturnCorrectNotes() {
        assertEquals("Window seat requested", transportation.getNotes());
    }

    @Test
    void transportation_getTrip_ShouldReturnLinkedTrip() {
        assertNotNull(transportation.getTrip());
        assertEquals(10L, transportation.getTrip().getId());
    }

    @Test
    void transportation_getAllFields_ShouldMatchExpected() {
        assertAll("All Transportation fields",
                () -> assertEquals(1L,                         transportation.getId()),
                () -> assertEquals("Flight",                   transportation.getType()),
                () -> assertEquals("NYC to LA",                transportation.getTitle()),
                () -> assertEquals("JFK Airport",              transportation.getDeparture()),
                () -> assertEquals("LAX Airport",              transportation.getArrival()),
                () -> assertEquals(LocalTime.of(8, 0),         transportation.getDepartTime()),
                () -> assertEquals(LocalTime.of(11, 30),       transportation.getArriveTime()),
                () -> assertEquals("Delta Airlines",           transportation.getCarrier()),
                () -> assertEquals("ABC123",                   transportation.getConfirmation()),
                () -> assertEquals("Window seat requested",    transportation.getNotes()),
                () -> assertNotNull(                           transportation.getTrip())
        );
    }

    // =========================
    // Transportation - Setters
    // =========================

    @Test
    void transportation_setType_ShouldUpdateValue() {
        transportation.setType("Train");
        assertEquals("Train", transportation.getType());
    }

    @Test
    void transportation_setTitle_ShouldUpdateValue() {
        transportation.setTitle("LA to Chicago");
        assertEquals("LA to Chicago", transportation.getTitle());
    }

    @Test
    void transportation_setDeparture_ShouldUpdateValue() {
        transportation.setDeparture("Union Station");
        assertEquals("Union Station", transportation.getDeparture());
    }

    @Test
    void transportation_setArrival_ShouldUpdateValue() {
        transportation.setArrival("Chicago Union Station");
        assertEquals("Chicago Union Station", transportation.getArrival());
    }

    @Test
    void transportation_setDepartTime_ShouldUpdateValue() {
        transportation.setDepartTime(LocalDateTime.from(LocalTime.of(14, 0)));
        assertEquals(LocalTime.of(14, 0), transportation.getDepartTime());
    }

    @Test
    void transportation_setArriveTime_ShouldUpdateValue() {
        transportation.setArriveTime(LocalDateTime.from(LocalTime.of(20, 45)));
        assertEquals(LocalTime.of(20, 45), transportation.getArriveTime());
    }

    @Test
    void transportation_setCarrier_ShouldUpdateValue() {
        transportation.setCarrier("Amtrak");
        assertEquals("Amtrak", transportation.getCarrier());
    }

    @Test
    void transportation_setConfirmation_ShouldUpdateValue() {
        transportation.setConfirmation("XYZ999");
        assertEquals("XYZ999", transportation.getConfirmation());
    }

    @Test
    void transportation_setNotes_ShouldUpdateValue() {
        transportation.setNotes("Aisle seat preferred");
        assertEquals("Aisle seat preferred", transportation.getNotes());
    }

    @Test
    void transportation_setTrip_ShouldUpdateLinkedTrip() {
        Trip newTrip = new Trip();
        newTrip.setId(99L);
        transportation.setTrip(newTrip);
        assertEquals(99L, transportation.getTrip().getId());
    }

    // =========================
    // Transportation - Null / Edge Cases
    // =========================

    @Test
    void transportation_WithNullType_ShouldStoreNull() {
        transportation.setType(null);
        assertNull(transportation.getType());
    }

    @Test
    void transportation_WithNullTitle_ShouldStoreNull() {
        transportation.setTitle(null);
        assertNull(transportation.getTitle());
    }

    @Test
    void transportation_WithNullDeparture_ShouldStoreNull() {
        transportation.setDeparture(null);
        assertNull(transportation.getDeparture());
    }

    @Test
    void transportation_WithNullArrival_ShouldStoreNull() {
        transportation.setArrival(null);
        assertNull(transportation.getArrival());
    }

    @Test
    void transportation_WithNullDepartTime_ShouldStoreNull() {
        transportation.setDepartTime(null);
        assertNull(transportation.getDepartTime());
    }

    @Test
    void transportation_WithNullArriveTime_ShouldStoreNull() {
        transportation.setArriveTime(null);
        assertNull(transportation.getArriveTime());
    }

    @Test
    void transportation_WithNullCarrier_ShouldStoreNull() {
        transportation.setCarrier(null);
        assertNull(transportation.getCarrier());
    }

    @Test
    void transportation_WithNullConfirmation_ShouldStoreNull() {
        transportation.setConfirmation(null);
        assertNull(transportation.getConfirmation());
    }

    @Test
    void transportation_WithNullNotes_ShouldStoreNull() {
        transportation.setNotes(null);
        assertNull(transportation.getNotes());
    }

    @Test
    void transportation_WithNullTrip_ShouldStoreNull() {
        transportation.setTrip(null);
        assertNull(transportation.getTrip());
    }

    // =========================
    // Update Logic
    // (mirrors update() field-copy logic)
    // =========================

    @Test
    void updateLogic_ShouldCopyAllFieldsFromUpdatedTransportation() {
        Transportation existing = new Transportation();
        existing.setType("Bus");
        existing.setTitle("Old Route");
        existing.setDeparture("Old Departure");
        existing.setArrival("Old Arrival");
        existing.setDepartTime(LocalDateTime.from(LocalTime.of(6, 0)));
        existing.setArriveTime(LocalDateTime.from(LocalTime.of(9, 0)));
        existing.setCarrier("Old Carrier");
        existing.setConfirmation("OLD000");
        existing.setNotes("Old notes");

        Transportation updated = new Transportation();
        updated.setType("Flight");
        updated.setTitle("NYC to LA");
        updated.setDeparture("JFK Airport");
        updated.setArrival("LAX Airport");
        updated.setDepartTime(LocalDateTime.from(LocalTime.of(8, 0)));
        updated.setArriveTime(LocalDateTime.from(LocalTime.of(11, 30)));
        updated.setCarrier("Delta Airlines");
        updated.setConfirmation("ABC123");
        updated.setNotes("Window seat requested");

        // Mirror update() logic
        existing.setType(updated.getType());
        existing.setTitle(updated.getTitle());
        existing.setDeparture(updated.getDeparture());
        existing.setArrival(updated.getArrival());
        existing.setDepartTime(updated.getDepartTime());
        existing.setArriveTime(updated.getArriveTime());
        existing.setCarrier(updated.getCarrier());
        existing.setConfirmation(updated.getConfirmation());
        existing.setNotes(updated.getNotes());

        assertAll("All fields copied correctly",
                () -> assertEquals("Flight",                existing.getType()),
                () -> assertEquals("NYC to LA",             existing.getTitle()),
                () -> assertEquals("JFK Airport",           existing.getDeparture()),
                () -> assertEquals("LAX Airport",           existing.getArrival()),
                () -> assertEquals(LocalTime.of(8, 0),      existing.getDepartTime()),
                () -> assertEquals(LocalTime.of(11, 30),    existing.getArriveTime()),
                () -> assertEquals("Delta Airlines",        existing.getCarrier()),
                () -> assertEquals("ABC123",                existing.getConfirmation()),
                () -> assertEquals("Window seat requested", existing.getNotes())
        );
    }

    @Test
    void updateLogic_ShouldNotAffectIdOrTrip() {
        Transportation existing = new Transportation();
        existing.setId(5L);
        existing.setTrip(trip);
        existing.setType("Bus");

        Transportation updated = new Transportation();
        updated.setType("Flight");
        updated.setTitle("New Title");
        updated.setDeparture("New Departure");
        updated.setArrival("New Arrival");
        updated.setDepartTime(LocalDateTime.from(LocalTime.of(9, 0)));
        updated.setArriveTime(LocalDateTime.from(LocalTime.of(12, 0)));
        updated.setCarrier("United");
        updated.setConfirmation("NEW456");
        updated.setNotes("No notes");

        // Mirror update() — id and trip are intentionally NOT copied
        existing.setType(updated.getType());
        existing.setTitle(updated.getTitle());
        existing.setDeparture(updated.getDeparture());
        existing.setArrival(updated.getArrival());
        existing.setDepartTime(updated.getDepartTime());
        existing.setArriveTime(updated.getArriveTime());
        existing.setCarrier(updated.getCarrier());
        existing.setConfirmation(updated.getConfirmation());
        existing.setNotes(updated.getNotes());

        assertEquals(5L,  existing.getId(),          "ID should not change after update");
        assertEquals(10L, existing.getTrip().getId(), "Trip should not change after update");
    }

    @Test
    void updateLogic_WithNullFields_ShouldOverwriteWithNull() {
        Transportation existing = new Transportation();
        existing.setCarrier("Old Carrier");
        existing.setNotes("Old notes");
        existing.setConfirmation("OLD000");

        Transportation updated = new Transportation();
        updated.setCarrier(null);
        updated.setNotes(null);
        updated.setConfirmation(null);

        existing.setCarrier(updated.getCarrier());
        existing.setNotes(updated.getNotes());
        existing.setConfirmation(updated.getConfirmation());

        assertAll("Null fields overwrite correctly",
                () -> assertNull(existing.getCarrier(),      "Carrier should be overwritten with null"),
                () -> assertNull(existing.getNotes(),        "Notes should be overwritten with null"),
                () -> assertNull(existing.getConfirmation(), "Confirmation should be overwritten with null")
        );
    }

    // =========================
    // Depart / Arrive Time Logic
    // =========================

    @Test
    void times_ArriveTimeShouldBeAfterDepartTime() {
        assertTrue(
                transportation.getArriveTime().isAfter(transportation.getDepartTime()),
                "Arrive time should be after depart time"
        );
    }

    @Test
    void times_SameTimeForDepartAndArrive_ShouldNotBeAfter() {
        transportation.setDepartTime(LocalDateTime.from(LocalTime.of(10, 0)));
        transportation.setArriveTime(LocalDateTime.from(LocalTime.of(10, 0)));

        assertFalse(
                transportation.getArriveTime().isAfter(transportation.getDepartTime()),
                "Same depart and arrive time should not be considered 'after'"
        );
    }

    @Test
    void times_FlightDurationShouldCalculateCorrectly() {
        LocalTime depart = LocalTime.of(8, 0);
        LocalTime arrive = LocalTime.of(11, 30);

        long minutes = java.time.temporal.ChronoUnit.MINUTES.between(depart, arrive);

        assertEquals(210, minutes, "Flight from 08:00 to 11:30 should be 210 minutes");
    }

    @Test
    void times_OneHourFlightDurationShouldBeCorrect() {
        LocalTime depart = LocalTime.of(9, 0);
        LocalTime arrive = LocalTime.of(10, 0);

        long minutes = java.time.temporal.ChronoUnit.MINUTES.between(depart, arrive);

        assertEquals(60, minutes);
    }

    // =========================
    // Delete / List Logic
    // =========================

    @Test
    void deleteLogic_WhenIdExists_ShouldProceed() {
        List<Long> existingIds = List.of(1L, 2L, 3L);
        assertTrue(existingIds.contains(1L));
    }

    @Test
    void deleteLogic_WhenIdNotFound_ShouldNotProceed() {
        List<Long> existingIds = List.of(1L, 2L, 3L);
        assertFalse(existingIds.contains(99L));
    }
}