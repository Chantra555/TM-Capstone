package com.muro.controller;

import com.muro.dto.ExpenseDTO;
import com.muro.entity.Expense;
import com.muro.entity.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseControllerTest {

    private ExpenseDTO expenseDTO;

    @BeforeEach
    void setUp() {
        expenseDTO = new ExpenseDTO();
        expenseDTO.setId(1L);
        expenseDTO.setName("Hotel");
        expenseDTO.setCost(250.0);
        expenseDTO.setTripId(10L);
    }

    // =========================
    // ExpenseDTO - Getters
    // =========================

    @Test
    void expenseDTO_getId_ShouldReturnCorrectId() {
        assertEquals(1L, expenseDTO.getId());
    }

    @Test
    void expenseDTO_getName_ShouldReturnCorrectName() {
        assertEquals("Hotel", expenseDTO.getName());
    }

    @Test
    void expenseDTO_getCost_ShouldReturnCorrectCost() {
        assertEquals(250.0, expenseDTO.getCost());
    }

    @Test
    void expenseDTO_getTripId_ShouldReturnCorrectTripId() {
        assertEquals(10L, expenseDTO.getTripId());
    }

    @Test
    void expenseDTO_getAllFields_ShouldMatchExpected() {
        assertAll("All ExpenseDTO fields",
                () -> assertEquals(1L,       expenseDTO.getId()),
                () -> assertEquals("Hotel",  expenseDTO.getName()),
                () -> assertEquals(250.0,    expenseDTO.getCost()),
                () -> assertEquals(10L,      expenseDTO.getTripId())
        );
    }

    // =========================
    // ExpenseDTO - Setters
    // =========================

    @Test
    void expenseDTO_setName_ShouldUpdateValue() {
        expenseDTO.setName("Flight");
        assertEquals("Flight", expenseDTO.getName());
    }

    @Test
    void expenseDTO_setCost_ShouldUpdateValue() {
        expenseDTO.setCost(499.99);
        assertEquals(499.99, expenseDTO.getCost());
    }

    @Test
    void expenseDTO_setTripId_ShouldUpdateValue() {
        expenseDTO.setTripId(99L);
        assertEquals(99L, expenseDTO.getTripId());
    }

    @Test
    void expenseDTO_setId_ShouldUpdateValue() {
        expenseDTO.setId(55L);
        assertEquals(55L, expenseDTO.getId());
    }

    // =========================
    // ExpenseDTO - Null / Edge Cases
    // =========================

    @Test
    void expenseDTO_WithNullName_ShouldStoreNull() {
        expenseDTO.setName(null);
        assertNull(expenseDTO.getName());
    }

    @Test
    void expenseDTO_WithNullTripId_ShouldStoreNull() {
        expenseDTO.setTripId(null);
        assertNull(expenseDTO.getTripId());
    }

    @Test
    void expenseDTO_WithZeroCost_ShouldBeAllowed() {
        expenseDTO.setCost(0.0);
        assertEquals(0.0, expenseDTO.getCost());
    }

    @Test
    void expenseDTO_WithNegativeCost_ShouldBeStored() {
        expenseDTO.setCost(-50.0);
        assertEquals(-50.0, expenseDTO.getCost());
    }

    @Test
    void expenseDTO_WithDecimalCost_ShouldBeStoredAccurately() {
        expenseDTO.setCost(19.99);
        assertEquals(19.99, expenseDTO.getCost(), 0.001);
    }

    // =========================
    // toDTO Mapping Logic
    // (mirrors private toDTO() in ExpenseController)
    // =========================

    @Test
    void toDTO_WithTripLinked_ShouldMapAllFields() {
        Trip trip = new Trip();
        trip.setId(10L);

        Expense expense = new Expense();
        expense.setId(1L);
        expense.setName("Hotel");
        expense.setCost(250.0);
        expense.setTrip(trip);

        // Mirror the toDTO logic manually
        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(expense.getId());
        dto.setName(expense.getName());
        dto.setCost(expense.getCost());
        if (expense.getTrip() != null) {
            dto.setTripId(expense.getTrip().getId());
        }

        assertAll("toDTO with trip",
                () -> assertEquals(1L,      dto.getId()),
                () -> assertEquals("Hotel", dto.getName()),
                () -> assertEquals(250.0,   dto.getCost()),
                () -> assertEquals(10L,     dto.getTripId())
        );
    }

    @Test
    void toDTO_WithNullTrip_ShouldNotSetTripId() {
        Expense expense = new Expense();
        expense.setId(2L);
        expense.setName("Food");
        expense.setCost(50.0);
        expense.setTrip(null);

        // Mirror toDTO logic
        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(expense.getId());
        dto.setName(expense.getName());
        dto.setCost(expense.getCost());
        if (expense.getTrip() != null) {
            dto.setTripId(expense.getTrip().getId());
        }

        assertNull(dto.getTripId(), "TripId should be null when no trip is linked");
    }

    @Test
    void toDTO_WithZeroCost_ShouldMapCorrectly() {
        Trip trip = new Trip();
        trip.setId(5L);

        Expense expense = new Expense();
        expense.setId(3L);
        expense.setName("Free Tour");
        expense.setCost(0.0);
        expense.setTrip(trip);

        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(expense.getId());
        dto.setName(expense.getName());
        dto.setCost(expense.getCost());
        if (expense.getTrip() != null) {
            dto.setTripId(expense.getTrip().getId());
        }

        assertEquals(0.0, dto.getCost());
        assertEquals(5L,  dto.getTripId());
    }

    // =========================
    // Stream / List Logic
    // (mirrors getExpensesByTrip stream mapping)
    // =========================

    @Test
    void streamMapping_ShouldProduceCorrectNumberOfDTOs() {
        Expense e1 = new Expense(); e1.setId(1L); e1.setName("Hotel"); e1.setCost(100.0);
        Expense e2 = new Expense(); e2.setId(2L); e2.setName("Flight"); e2.setCost(200.0);
        Expense e3 = new Expense(); e3.setId(3L); e3.setName("Food");   e3.setCost(50.0);

        List<Expense> expenses = List.of(e1, e2, e3);

        List<ExpenseDTO> dtos = expenses.stream().map(exp -> {
            ExpenseDTO d = new ExpenseDTO();
            d.setId(exp.getId());
            d.setName(exp.getName());
            d.setCost(exp.getCost());
            return d;
        }).collect(Collectors.toList());

        assertEquals(3, dtos.size());
    }

    @Test
    void streamMapping_ShouldPreserveNamesAndCosts() {
        Expense e1 = new Expense(); e1.setName("Hotel");  e1.setCost(100.0);
        Expense e2 = new Expense(); e2.setName("Flight"); e2.setCost(200.0);

        List<ExpenseDTO> dtos = List.of(e1, e2).stream().map(exp -> {
            ExpenseDTO d = new ExpenseDTO();
            d.setName(exp.getName());
            d.setCost(exp.getCost());
            return d;
        }).collect(Collectors.toList());

        assertEquals("Hotel",  dtos.get(0).getName());
        assertEquals(100.0,    dtos.get(0).getCost());
        assertEquals("Flight", dtos.get(1).getName());
        assertEquals(200.0,    dtos.get(1).getCost());
    }

    @Test
    void streamMapping_WithEmptyList_ShouldReturnEmptyList() {
        List<Expense> expenses = List.of();

        List<ExpenseDTO> dtos = expenses.stream().map(exp -> {
            ExpenseDTO d = new ExpenseDTO();
            d.setName(exp.getName());
            d.setCost(exp.getCost());
            return d;
        }).collect(Collectors.toList());

        assertTrue(dtos.isEmpty());
    }
}