package com.muro.dto;

import java.util.List;

public class BudgetDTO {

    private Long budgetId;
    private Double idealBudget;

    private Double spent;
    private Double remaining;

    private List<ExpenseDTO> expenses;

    /* ================= GETTERS / SETTERS ================= */

    public Long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(Long budgetId) {
        this.budgetId = budgetId;
    }

    public Double getIdealBudget() {
        return idealBudget;
    }

    public void setIdealBudget(Double idealBudget) {
        this.idealBudget = idealBudget;
    }

    public Double getSpent() {
        return spent;
    }

    public void setSpent(Double spent) {
        this.spent = spent;
    }

    public Double getRemaining() {
        return remaining;
    }

    public void setRemaining(Double remaining) {
        this.remaining = remaining;
    }

    public List<ExpenseDTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseDTO> expenses) {
        this.expenses = expenses;
    }
}
