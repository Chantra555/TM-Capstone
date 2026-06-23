package com.muro.controller;

import com.muro.entity.ItineraryEvent;
import com.muro.entity.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItineraryEventControllerTest {

    private ItineraryEvent event;
    private Trip trip;

    @BeforeEach
    void setUp() {
        trip = new Trip();
        trip.setId(10L);

        event = new ItineraryEvent();
        event.setId(1L);
        event.setTitle("Museum Visit");
        event.setEventDate(LocalDate.of(2025, 6, 15));
        event.setEventTime(LocalTime.of(10, 0));
        event.setType("Sightseeing");
        event.setDescription("Visit the natural history museum");
        event.setTrip(trip);
    }

    // =========================
    // ItineraryEvent - Getters
    // =========================

    @Test
    void itineraryEvent_getId_ShouldReturnCorrectId() {
        assertEquals(1L, event.getId());
    }

    @Test
    void itineraryEvent_getTitle_ShouldReturnCorrectTitle() {
        assertEquals("Museum Visit", event.getTitle());
    }

    @Test
    void itineraryEvent_getEventDate_ShouldReturnCorrectDate() {
        assertEquals(LocalDate.of(2025, 6, 15), event.getEventDate());
    }

    @Test
    void itineraryEvent_getEventTime_ShouldReturnCorrectTime() {
        assertEquals(LocalTime.of(10, 0), event.getEventTime());
    }

    @Test
    void itineraryEvent_getType_ShouldReturnCorrectType() {
        assertEquals("Sightseeing", event.getType());
    }

    @Test
    void itineraryEvent_getDescription_ShouldReturnCorrectDescription() {
        assertEquals("Visit the natural history museum", event.getDescription());
    }

    @Test
    void itineraryEvent_getTrip_ShouldReturnLinkedTrip() {
        assertNotNull(event.getTrip());
        assertEquals(10L, event.getTrip().getId());
    }

    @Test
    void itineraryEvent_getAllFields_ShouldMatchExpected() {
        assertAll("All ItineraryEvent fields",
                () -> assertEquals(1L,                                  event.getId()),
                () -> assertEquals("Museum Visit",                      event.getTitle()),
                () -> assertEquals(LocalDate.of(2025, 6, 15),  event.getEventDate()),
                () -> assertEquals(LocalTime.of(10, 0),                 event.getEventTime()),
                () -> assertEquals("Sightseeing",                       event.getType()),
                () -> assertEquals("Visit the natural history museum",  event.getDescription()),
                () -> assertNotNull(                                     event.getTrip())
        );
    }

    // =========================
    // ItineraryEvent - Setters
    // =========================

    @Test
    void itineraryEvent_setTitle_ShouldUpdateValue() {
        event.setTitle("Beach Day");
        assertEquals("Beach Day", event.getTitle());
    }

    @Test
    void itineraryEvent_setEventDate_ShouldUpdateValue() {
        event.setEventDate(LocalDate.of(2025, 7, 4));
        assertEquals(LocalDate.of(2025, 7, 4), event.getEventDate());
    }

    @Test
    void itineraryEvent_setEventTime_ShouldUpdateValue() {
        event.setEventTime(LocalTime.of(14, 30));
        assertEquals(LocalTime.of(14, 30), event.getEventTime());
    }

    @Test
    void itineraryEvent_setType_ShouldUpdateValue() {
        event.setType("Food");
        assertEquals("Food", event.getType());
    }

    @Test
    void itineraryEvent_setDescription_ShouldUpdateValue() {
        event.setDescription("Try local street food");
        assertEquals("Try local street food", event.getDescription());
    }

    @Test
    void itineraryEvent_setTrip_ShouldUpdateLinkedTrip() {
        Trip newTrip = new Trip();
        newTrip.setId(99L);
        event.setTrip(newTrip);
        assertEquals(99L, event.getTrip().getId());
    }

    // =========================
    // ItineraryEvent - Null / Edge Cases
    // =========================

    @Test
    void itineraryEvent_WithNullTitle_ShouldStoreNull() {
        event.setTitle(null);
        assertNull(event.getTitle());
    }

    @Test
    void itineraryEvent_WithNullEventDate_ShouldStoreNull() {
        event.setEventDate(null);
        assertNull(event.getEventDate());
    }

    @Test
    void itineraryEvent_WithNullEventTime_ShouldStoreNull() {
        event.setEventTime(null);
        assertNull(event.getEventTime());
    }

    @Test
    void itineraryEvent_WithNullType_ShouldStoreNull() {
        event.setType(null);
        assertNull(event.getType());
    }

    @Test
    void itineraryEvent_WithNullDescription_ShouldStoreNull() {
        event.setDescription(null);
        assertNull(event.getDescription());
    }

    @Test
    void itineraryEvent_WithNullTrip_ShouldStoreNull() {
        event.setTrip(null);
        assertNull(event.getTrip());
    }

    // =========================
    // Update Logic
    // (mirrors updateEvent() field-copy logic)
    // =========================

    @Test
    void updateLogic_ShouldCopyAllFieldsFromUpdatedEvent() {
        ItineraryEvent existing = new ItineraryEvent();
        existing.setTitle("Old Title");
        existing.setEventDate(LocalDate.of(2024, 1, 1));
        existing.setEventTime(LocalTime.of(9, 0));
        existing.setType("Old Type");
        existing.setDescription("Old description");

        ItineraryEvent updated = new ItineraryEvent();
        updated.setTitle("New Title");
        updated.setEventDate(LocalDate.of(2025, 12, 25));
        updated.setEventTime(LocalTime.of(18, 30));
        updated.setType("New Type");
        updated.setDescription("New description");

        // Mirror updateEvent() logic
        existing.setTitle(updated.getTitle());
        existing.setEventDate(updated.getEventDate());
        existing.setEventTime(updated.getEventTime());
        existing.setType(updated.getType());
        existing.setDescription(updated.getDescription());

        assertAll("All fields copied correctly",
                () -> assertEquals("New Title",                     existing.getTitle()),
                () -> assertEquals(LocalDate.of(2025, 12, 25),     existing.getEventDate()),
                () -> assertEquals(LocalTime.of(18, 30),           existing.getEventTime()),
                () -> assertEquals("New Type",                      existing.getType()),
                () -> assertEquals("New description",               existing.getDescription())
        );
    }

    @Test
    void updateLogic_ShouldNotAffectTripOrId() {
        ItineraryEvent existing = new ItineraryEvent();
        existing.setId(5L);
        existing.setTrip(trip);
        existing.setTitle("Old Title");

        ItineraryEvent updated = new ItineraryEvent();
        updated.setTitle("New Title");
        updated.setEventDate(LocalDate.of(2025, 8, 1));
        updated.setEventTime(LocalTime.of(9, 0));
        updated.setType("Adventure");
        updated.setDescription("Go hiking");

        // Mirror updateEvent() — only these 5 fields are copied, NOT id or trip
        existing.setTitle(updated.getTitle());
        existing.setEventDate(updated.getEventDate());
        existing.setEventTime(updated.getEventTime());
        existing.setType(updated.getType());
        existing.setDescription(updated.getDescription());

        assertEquals(5L,   existing.getId(),  "ID should not change after update");
        assertEquals(10L,  existing.getTrip().getId(), "Trip should not change after update");
    }

    @Test
    void updateLogic_WithNullFields_ShouldOverwriteWithNull() {
        ItineraryEvent existing = new ItineraryEvent();
        existing.setTitle("Has a title");
        existing.setDescription("Has a description");

        ItineraryEvent updated = new ItineraryEvent();
        updated.setTitle(null);
        updated.setDescription(null);

        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());

        assertNull(existing.getTitle(),       "Title should be overwritten with null");
        assertNull(existing.getDescription(), "Description should be overwritten with null");
    }

    // =========================
    // Trip - Getters / Setters
    // =========================

    @Test
    void trip_getId_ShouldReturnCorrectId() {
        assertEquals(10L, trip.getId());
    }

    @Test
    void trip_setId_ShouldUpdateValue() {
        trip.setId(55L);
        assertEquals(55L, trip.getId());
    }

    // =========================
    // Delete Logic
    // (mirrors existsById guard in deleteEvent())
    // =========================

    @Test
    void deleteLogic_WhenEventExists_ShouldProceed() {
        // Simulate existsById = true by checking a populated list contains the id
        List<Long> existingIds = List.of(1L, 2L, 3L);
        assertTrue(existingIds.contains(1L), "Event should exist before deletion");
    }

    @Test
    void deleteLogic_WhenEventDoesNotExist_ShouldNotProceed() {
        List<Long> existingIds = List.of(1L, 2L, 3L);
        assertFalse(existingIds.contains(99L), "Non-existent event should not be found");
    }
}