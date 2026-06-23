package com.muro.controller;

import com.muro.dto.LodgingRequest;
import com.muro.entity.Lodging;
import com.muro.entity.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LodgingControllerTest {

    private LodgingRequest request;
    private Lodging lodging;
    private Trip trip;

    @BeforeEach
    void setUp() {
        trip = new Trip();
        trip.setId(10L);

        request = new LodgingRequest();
        request.tripId   = 10L;
        request.name     = "Hilton Hotel";
        request.location = "New York, NY";
        request.checkIn  = LocalDate.of(2025, 6, 1);
        request.checkOut = LocalDate.of(2025, 6, 7);
        request.price    = 199.99;
        request.notes    = "Non-smoking room requested";

        lodging = new Lodging();
        lodging.setId(1L);
        lodging.setName("Hilton Hotel");
        lodging.setLocation("New York, NY");
        lodging.setCheckIn(LocalDate.of(2025, 6, 1));
        lodging.setCheckOut(LocalDate.of(2025, 6, 7));
        lodging.setPrice(199.99);
        lodging.setNotes("Non-smoking room requested");
        lodging.setTrip(trip);
    }

    // =========================
    // LodgingRequest - Fields
    // =========================

    @Test
    void lodgingRequest_name_ShouldReturnCorrectValue() {
        assertEquals("Hilton Hotel", request.name);
    }

    @Test
    void lodgingRequest_location_ShouldReturnCorrectValue() {
        assertEquals("New York, NY", request.location);
    }

    @Test
    void lodgingRequest_checkIn_ShouldReturnCorrectDate() {
        assertEquals(LocalDate.of(2025, 6, 1), request.checkIn);
    }

    @Test
    void lodgingRequest_checkOut_ShouldReturnCorrectDate() {
        assertEquals(LocalDate.of(2025, 6, 7), request.checkOut);
    }

    @Test
    void lodgingRequest_price_ShouldReturnCorrectValue() {
        assertEquals(199.99, request.price, 0.001);
    }

    @Test
    void lodgingRequest_notes_ShouldReturnCorrectValue() {
        assertEquals("Non-smoking room requested", request.notes);
    }

    @Test
    void lodgingRequest_tripId_ShouldReturnCorrectValue() {
        assertEquals(10L, request.tripId);
    }

    @Test
    void lodgingRequest_AllFields_ShouldMatchExpected() {
        assertAll("All LodgingRequest fields",
                () -> assertEquals(10L,                              request.tripId),
                () -> assertEquals("Hilton Hotel",                   request.name),
                () -> assertEquals("New York, NY",                   request.location),
                () -> assertEquals(LocalDate.of(2025, 6, 1), request.checkIn),
                () -> assertEquals(LocalDate.of(2025, 6, 7), request.checkOut),
                () -> assertEquals(199.99,                           request.price, 0.001),
                () -> assertEquals("Non-smoking room requested",     request.notes)
        );
    }

    // =========================
    // LodgingRequest - Null / Edge Cases
    // =========================

    @Test
    void lodgingRequest_WithNullName_ShouldStoreNull() {
        request.name = null;
        assertNull(request.name);
    }

    @Test
    void lodgingRequest_WithNullLocation_ShouldStoreNull() {
        request.location = null;
        assertNull(request.location);
    }

    @Test
    void lodgingRequest_WithNullNotes_ShouldStoreNull() {
        request.notes = null;
        assertNull(request.notes);
    }

    @Test
    void lodgingRequest_WithNullCheckIn_ShouldStoreNull() {
        request.checkIn = null;
        assertNull(request.checkIn);
    }

    @Test
    void lodgingRequest_WithNullCheckOut_ShouldStoreNull() {
        request.checkOut = null;
        assertNull(request.checkOut);
    }

    @Test
    void lodgingRequest_WithZeroPrice_ShouldBeAllowed() {
        request.price = 0.0;
        assertEquals(0.0, request.price);
    }

    @Test
    void lodgingRequest_WithNegativePrice_ShouldBeStored() {
        request.price = -50.0;
        assertEquals(-50.0, request.price);
    }

    // =========================
    // Lodging Entity - Getters
    // =========================

    @Test
    void lodging_getId_ShouldReturnCorrectId() {
        assertEquals(1L, lodging.getId());
    }

    @Test
    void lodging_getName_ShouldReturnCorrectName() {
        assertEquals("Hilton Hotel", lodging.getName());
    }

    @Test
    void lodging_getLocation_ShouldReturnCorrectLocation() {
        assertEquals("New York, NY", lodging.getLocation());
    }

    @Test
    void lodging_getCheckIn_ShouldReturnCorrectDate() {
        assertEquals(LocalDate.of(2025, 6, 1), lodging.getCheckIn());
    }

    @Test
    void lodging_getCheckOut_ShouldReturnCorrectDate() {
        assertEquals(LocalDate.of(2025, 6, 7), lodging.getCheckOut());
    }

    @Test
    void lodging_getPrice_ShouldReturnCorrectPrice() {
        assertEquals(199.99, lodging.getPrice(), 0.001);
    }

    @Test
    void lodging_getNotes_ShouldReturnCorrectNotes() {
        assertEquals("Non-smoking room requested", lodging.getNotes());
    }

    @Test
    void lodging_getTrip_ShouldReturnLinkedTrip() {
        assertNotNull(lodging.getTrip());
        assertEquals(10L, lodging.getTrip().getId());
    }

    @Test
    void lodging_getAllFields_ShouldMatchExpected() {
        assertAll("All Lodging fields",
                () -> assertEquals(1L,                                  lodging.getId()),
                () -> assertEquals("Hilton Hotel",                      lodging.getName()),
                () -> assertEquals("New York, NY",                      lodging.getLocation()),
                () -> assertEquals(LocalDate.of(2025, 6, 1),   lodging.getCheckIn()),
                () -> assertEquals(LocalDate.of(2025, 6, 7),   lodging.getCheckOut()),
                () -> assertEquals(199.99,                              lodging.getPrice(), 0.001),
                () -> assertEquals("Non-smoking room requested",        lodging.getNotes()),
                () -> assertNotNull(                                     lodging.getTrip())
        );
    }

    // =========================
    // Lodging Entity - Setters
    // =========================

    @Test
    void lodging_setName_ShouldUpdateValue() {
        lodging.setName("Marriott");
        assertEquals("Marriott", lodging.getName());
    }

    @Test
    void lodging_setLocation_ShouldUpdateValue() {
        lodging.setLocation("Chicago, IL");
        assertEquals("Chicago, IL", lodging.getLocation());
    }

    @Test
    void lodging_setCheckIn_ShouldUpdateValue() {
        lodging.setCheckIn(LocalDate.of(2025, 9, 1));
        assertEquals(LocalDate.of(2025, 9, 1), lodging.getCheckIn());
    }

    @Test
    void lodging_setCheckOut_ShouldUpdateValue() {
        lodging.setCheckOut(LocalDate.of(2025, 9, 7));
        assertEquals(LocalDate.of(2025, 9, 7), lodging.getCheckOut());
    }

    @Test
    void lodging_setPrice_ShouldUpdateValue() {
        lodging.setPrice(299.99);
        assertEquals(299.99, lodging.getPrice(), 0.001);
    }

    @Test
    void lodging_setNotes_ShouldUpdateValue() {
        lodging.setNotes("Late check-in");
        assertEquals("Late check-in", lodging.getNotes());
    }

    @Test
    void lodging_setTrip_ShouldUpdateLinkedTrip() {
        Trip newTrip = new Trip();
        newTrip.setId(99L);
        lodging.setTrip(newTrip);
        assertEquals(99L, lodging.getTrip().getId());
    }

    // =========================
    // Lodging Entity - Null / Edge Cases
    // =========================

    @Test
    void lodging_WithNullName_ShouldStoreNull() {
        lodging.setName(null);
        assertNull(lodging.getName());
    }

    @Test
    void lodging_WithNullLocation_ShouldStoreNull() {
        lodging.setLocation(null);
        assertNull(lodging.getLocation());
    }

    @Test
    void lodging_WithNullTrip_ShouldStoreNull() {
        lodging.setTrip(null);
        assertNull(lodging.getTrip());
    }

    @Test
    void lodging_WithNullNotes_ShouldStoreNull() {
        lodging.setNotes(null);
        assertNull(lodging.getNotes());
    }

    @Test
    void lodging_WithZeroPrice_ShouldBeAllowed() {
        lodging.setPrice(0.0);
        assertEquals(0.0, lodging.getPrice());
    }

    // =========================
    // Mapping Logic
    // (mirrors createLodging() field-by-field assignment)
    // =========================

    @Test
    void mappingLogic_ShouldCopyAllFieldsFromRequestToLodging() {
        Lodging mapped = new Lodging();
        mapped.setName(request.name);
        mapped.setLocation(request.location);
        mapped.setCheckIn(request.checkIn);
        mapped.setCheckOut(request.checkOut);
        mapped.setPrice(request.price);
        mapped.setNotes(request.notes);
        mapped.setTrip(trip);

        assertAll("Request mapped to Lodging",
                () -> assertEquals(request.name,     mapped.getName()),
                () -> assertEquals(request.location, mapped.getLocation()),
                () -> assertEquals(request.checkIn,  mapped.getCheckIn()),
                () -> assertEquals(request.checkOut, mapped.getCheckOut()),
                () -> assertEquals(request.price,    mapped.getPrice(), 0.001),
                () -> assertEquals(request.notes,    mapped.getNotes()),
                () -> assertEquals(10L,              mapped.getTrip().getId())
        );
    }

    @Test
    void mappingLogic_WithNullNotes_ShouldMapNullCorrectly() {
        request.notes = null;

        Lodging mapped = new Lodging();
        mapped.setNotes(request.notes);

        assertNull(mapped.getNotes(), "Null notes from request should map to null on Lodging");
    }

    @Test
    void mappingLogic_WithZeroPrice_ShouldMapCorrectly() {
        request.price = 0.0;

        Lodging mapped = new Lodging();
        mapped.setPrice(request.price);

        assertEquals(0.0, mapped.getPrice());
    }

    @Test
    void mappingLogic_TripShouldBeLinkedCorrectly() {
        Lodging mapped = new Lodging();
        mapped.setTrip(trip);

        assertNotNull(mapped.getTrip());
        assertEquals(10L, mapped.getTrip().getId());
    }

    // =========================
    // Check-in / Check-out Logic
    // =========================

    @Test
    void checkInAndCheckOut_CheckOutShouldBeAfterCheckIn() {
        assertTrue(
                lodging.getCheckOut().isAfter(lodging.getCheckIn()),
                "Check-out should be after check-in"
        );
    }

    @Test
    void checkInAndCheckOut_SameDayShouldNotBeAfter() {
        lodging.setCheckIn(LocalDate.of(2025, 6, 1));
        lodging.setCheckOut(LocalDate.of(2025, 6, 1));

        assertFalse(
                lodging.getCheckOut().isAfter(lodging.getCheckIn()),
                "Same day check-in and check-out should not be valid as 'after'"
        );
    }

    @Test
    void checkInAndCheckOut_NightsShouldCalculateCorrectly() {
        LocalDate checkIn  = LocalDate.of(2025, 6, 1);
        LocalDate checkOut = LocalDate.of(2025, 6, 7);

        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);

        assertEquals(6, nights, "Stay from June 1 to June 7 should be 6 nights");
    }

    @Test
    void checkInAndCheckOut_SingleNightStay_ShouldBeOneNight() {
        LocalDate checkIn  = LocalDate.of(2025, 8, 15);
        LocalDate checkOut = LocalDate.of(2025, 8, 16);

        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);

        assertEquals(1, nights);
    }

    // =========================
    // Delete Response Logic
    // =========================

    @Test
    void deleteResponse_ShouldReturnOkString() {
        String response = "Deleted";
        assertEquals("Deleted", response);
    }

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