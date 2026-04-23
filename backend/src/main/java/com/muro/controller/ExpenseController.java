package com.muro.controller;

import com.muro.dto.ExpenseDTO;
import com.muro.entity.Expense;
import com.muro.entity.Trip;
import com.muro.repository.ExpenseRepository;
import com.muro.repository.TripRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expense")
@CrossOrigin(origins = "http://localhost:5173")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;
    private final TripRepository tripRepository;

    public ExpenseController(
            ExpenseRepository expenseRepository,
            TripRepository tripRepository
    ) {
        this.expenseRepository = expenseRepository;
        this.tripRepository = tripRepository;
    }

    /* ================= GET EXPENSES BY TRIP ================= */

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByTrip(@PathVariable Long tripId) {

        List<ExpenseDTO> expenses = expenseRepository
                .findByTripId(tripId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(expenses);
    }

    /* ================= CREATE EXPENSE ================= */

    @PostMapping
    public ResponseEntity<ExpenseDTO> createExpense(@RequestBody ExpenseDTO dto) {

        Expense expense = new Expense();
        expense.setName(dto.getName());
        expense.setCost(dto.getCost());

        // 🔥 IMPORTANT: link to Trip
        if (dto.getTripId() != null) {
            Trip trip = tripRepository.findById(dto.getTripId())
                    .orElseThrow(() -> new RuntimeException("Trip not found"));

            expense.setTrip(trip);
        }

        Expense saved = expenseRepository.save(expense);

        return ResponseEntity.ok(toDTO(saved));
    }

    /* ================= UPDATE EXPENSE ================= */

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDTO> updateExpense(
            @PathVariable Long id,
            @RequestBody ExpenseDTO dto
    ) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        expense.setName(dto.getName());
        expense.setCost(dto.getCost());

        Expense updated = expenseRepository.save(expense);

        return ResponseEntity.ok(toDTO(updated));
    }

    /* ================= DELETE EXPENSE ================= */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /* ================= MAPPER ================= */

    private ExpenseDTO toDTO(Expense exp) {
        ExpenseDTO dto = new ExpenseDTO();

        dto.setId(exp.getId());
        dto.setName(exp.getName());
        dto.setCost(exp.getCost());

        if (exp.getTrip() != null) {
            dto.setTripId(exp.getTrip().getId());
        }

        return dto;
    }
}
