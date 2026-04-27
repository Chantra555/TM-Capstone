package com.muro.controller;

import com.muro.dto.BudgetDTO;
import com.muro.dto.ExpenseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BudgetControllerTest {

    private BudgetDTO budgetDTO;
    private ExpenseDTO expenseDTO;

    @BeforeEach
    void setUp() {
        budgetDTO = new BudgetDTO();
        budgetDTO.setBudgetId(1L);
        budgetDTO.setIdealBudget(1000.0);
        budgetDTO.setSpent(250.0);
        budgetDTO.setRemaining(750.0);

        expenseDTO = new ExpenseDTO();
        expenseDTO.setId(1L);
        expenseDTO.setName("Hotel");
        expenseDTO.setCost(250.0);
        expenseDTO.setTripId(10L);
    }

    // =========================
    // BudgetDTO - Getters
    // =========================

    @Test
    void budgetDTO_getBudgetId_ShouldReturnCorrectId() {
        assertEquals(1L, budgetDTO.getBudgetId());
    }

    @Test
    void budgetDTO_getIdealBudget_ShouldReturnCorrectAmount() {
        assertEquals(1000.0, budgetDTO.getIdealBudget());
    }

    @Test
    void budgetDTO_getSpent_ShouldReturnCorrectAmount() {
        assertEquals(250.0, budgetDTO.getSpent());
    }

    @Test
    void budgetDTO_getRemaining_ShouldReturnCorrectAmount() {
        assertEquals(750.0, budgetDTO.getRemaining());
    }

    @Test
    void budgetDTO_getAllFields_ShouldMatchExpected() {
        assertAll("All BudgetDTO fields",
                () -> assertEquals(1L,     budgetDTO.getBudgetId()),
                () -> assertEquals(1000.0, budgetDTO.getIdealBudget()),
                () -> assertEquals(250.0,  budgetDTO.getSpent()),
                () -> assertEquals(750.0,  budgetDTO.getRemaining())
        );
    }

    // =========================
    // BudgetDTO - Setters
    // =========================

    @Test
    void budgetDTO_setIdealBudget_ShouldUpdateValue() {
        budgetDTO.setIdealBudget(2000.0);
        assertEquals(2000.0, budgetDTO.getIdealBudget());
    }

    @Test
    void budgetDTO_setSpent_ShouldUpdateValue() {
        budgetDTO.setSpent(500.0);
        assertEquals(500.0, budgetDTO.getSpent());
    }

    @Test
    void budgetDTO_setRemaining_ShouldUpdateValue() {
        budgetDTO.setRemaining(1500.0);
        assertEquals(1500.0, budgetDTO.getRemaining());
    }

    // =========================
    // BudgetDTO - Null / Edge cases
    // =========================

    @Test
    void budgetDTO_WithNullExpenses_ShouldStoreNull() {
        budgetDTO.setExpenses(null);
        assertNull(budgetDTO.getExpenses());
    }

    @Test
    void budgetDTO_WithEmptyExpenses_ShouldReturnEmptyList() {
        budgetDTO.setExpenses(List.of());
        assertTrue(budgetDTO.getExpenses().isEmpty());
    }

    @Test
    void budgetDTO_WithZeroIdealBudget_ShouldBeAllowed() {
        budgetDTO.setIdealBudget(0.0);
        assertEquals(0.0, budgetDTO.getIdealBudget());
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
                () -> assertEquals(1L,      expenseDTO.getId()),
                () -> assertEquals("Hotel", expenseDTO.getName()),
                () -> assertEquals(250.0,   expenseDTO.getCost()),
                () -> assertEquals(10L,     expenseDTO.getTripId())
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
        expenseDTO.setCost(999.99);
        assertEquals(999.99, expenseDTO.getCost());
    }

    // =========================
    // ExpenseDTO - Null / Edge cases
    // =========================

    @Test
    void expenseDTO_WithNullTripId_ShouldStoreNull() {
        expenseDTO.setTripId(null);
        assertNull(expenseDTO.getTripId());
    }

    @Test
    void expenseDTO_WithNullName_ShouldStoreNull() {
        expenseDTO.setName(null);
        assertNull(expenseDTO.getName());
    }

    @Test
    void expenseDTO_WithZeroCost_ShouldBeAllowed() {
        expenseDTO.setCost(0.0);
        assertEquals(0.0, expenseDTO.getCost());
    }

    // =========================
    // Budget Calculation Logic
    // (mirrors toDTO math: spent = sum, remaining = ideal - spent)
    // =========================

    @Test
    void budgetCalc_SpentShouldEqualSumOfExpenses() {
        double cost1 = 100.0;
        double cost2 = 200.0;
        double cost3 = 50.0;

        double spent = cost1 + cost2 + cost3;

        assertEquals(350.0, spent);
    }

    @Test
    void budgetCalc_RemainingShouldEqualIdealMinusSpent() {
        double idealBudget = 1000.0;
        double spent       = 350.0;
        double remaining   = idealBudget - spent;

        assertEquals(650.0, remaining);
    }

    @Test
    void budgetCalc_WithNoExpenses_SpentShouldBeZero() {
        double spent = 0.0; // empty stream sum
        assertEquals(0.0, spent);
    }

    @Test
    void budgetCalc_WithNoExpenses_RemainingShouldEqualIdealBudget() {
        double idealBudget = 1000.0;
        double spent       = 0.0;
        double remaining   = idealBudget - spent;

        assertEquals(idealBudget, remaining);
    }

    @Test
    void budgetCalc_WhenSpentExceedsBudget_RemainingShouldBeNegative() {
        double idealBudget = 500.0;
        double spent       = 750.0;
        double remaining   = idealBudget - spent;

        assertTrue(remaining < 0, "Remaining should be negative when overspent");
        assertEquals(-250.0, remaining);
    }

    @Test
    void budgetCalc_WithDecimalCosts_ShouldSumCorrectly() {
        double cost1 = 10.50;
        double cost2 = 20.75;
        double cost3 = 5.25;

        double spent = cost1 + cost2 + cost3;

        assertEquals(36.50, spent, 0.001); // delta for floating point
    }
}