package com.muro.controller;

import com.muro.dto.BudgetDTO;
import com.muro.dto.ExpenseDTO;
import com.muro.entity.Budget;
import com.muro.entity.Expense;
import com.muro.entity.Trip;
import com.muro.repository.BudgetRepository;
import com.muro.repository.ExpenseRepository;
import com.muro.repository.TripRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/budget")
@CrossOrigin(origins = "http://localhost:5173")
public class BudgetController {

    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;
    private final TripRepository tripRepository;

    public BudgetController(
            BudgetRepository budgetRepository,
            ExpenseRepository expenseRepository,
            TripRepository tripRepository
    ) {
        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
        this.tripRepository = tripRepository;
    }

    /* ================= CREATE BUDGET ================= */

    @PostMapping("/{tripId}")
    public ResponseEntity<BudgetDTO> createBudget(
            @PathVariable Long tripId,
            @RequestBody BudgetDTO dto
    ) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        Budget budget = new Budget();
        budget.setIdealBudget(dto.getIdealBudget());
        budget.setTrip(trip);

        Budget saved = budgetRepository.save(budget);

        return ResponseEntity.ok(toDTO(saved));
    }

    /* ================= GET BUDGET BY TRIP ================= */

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<BudgetDTO> getBudgetByTrip(@PathVariable Long tripId) {

        Budget budget = budgetRepository.findByTripId(tripId)
                .orElse(null);

        if (budget == null) {
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(toDTO(budget));
    }

    /* ================= UPDATE BUDGET ================= */

    @PutMapping("/{id}")
    public ResponseEntity<BudgetDTO> updateBudget(
            @PathVariable Long id,
            @RequestBody BudgetDTO dto
    ) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        budget.setIdealBudget(dto.getIdealBudget());

        Budget updated = budgetRepository.save(budget);

        return ResponseEntity.ok(toDTO(updated));
    }

    /* ================= DELETE BUDGET ================= */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        budgetRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /* ================= DTO MAPPER ================= */

    private BudgetDTO toDTO(Budget budget) {

        Long tripId = budget.getTrip().getId();

        List<Expense> expenses =
                expenseRepository.findByTripId(tripId);

        double spent = expenses.stream()
                .mapToDouble(Expense::getCost)
                .sum();

        double remaining = budget.getIdealBudget() - spent;

        BudgetDTO dto = new BudgetDTO();
        dto.setBudgetId(budget.getId());
        dto.setIdealBudget(budget.getIdealBudget());
        dto.setSpent(spent);
        dto.setRemaining(remaining);

        List<ExpenseDTO> expenseDTOs = expenses.stream().map(exp -> {
            ExpenseDTO e = new ExpenseDTO();
            e.setId(exp.getId());
            e.setName(exp.getName());
            e.setCost(exp.getCost());

            // ✅ FIXED: use trip, NOT budget
            e.setTripId(exp.getTrip() != null ? exp.getTrip().getId() : null);

            return e;
        }).collect(Collectors.toList());

        dto.setExpenses(expenseDTOs);

        return dto;
    }
}
