
import "./Budget.css";
import { useState } from "react";

export default function Budget() {
  const [editMode, setEditMode] = useState(false);

  // 💳 expenses always exist
  const [expenses, setExpenses] = useState([
    { name: "Flights", amount: 500 },
    { name: "Hotel", amount: 300 },
    { name: "Food", amount: 120 },
    { name: "Activities", amount: 100 },
  ]);

  // 💰 optional budget
  const [budgetTotal, setBudgetTotal] = useState(null);

  // ➕ input for creating budget
  const [newBudget, setNewBudget] = useState("");

  // 🧠 calculations
  const spent = expenses.reduce(
    (sum, item) => sum + Number(item.amount || 0),
    0
  );

  const remaining = budgetTotal ? budgetTotal - spent : null;
  const hasBudget = budgetTotal && budgetTotal > 0;

  // ✏️ update budget total (edit mode)
  const handleBudgetChange = (e) => {
    setBudgetTotal(Number(e.target.value));
  };

  // ✏️ update expense
  const handleExpenseChange = (index, value) => {
    const updated = [...expenses];
    updated[index].amount = Number(value);
    setExpenses(updated);
  };

  // ➕ create budget
  const handleCreateBudget = () => {
    if (!newBudget) {
      alert("Must add in an amount");
    
      return;
    }

    

    setBudgetTotal(Number(newBudget));
    setNewBudget("");
  };
  const handleRemoveBudget = () => {
  setBudgetTotal(null);
  setEditMode(false);
};
const [newExpense, setNewExpense] = useState({
  name: "",
  amount: "",
});
const handleAddExpense = () => {
  if (!newExpense.name && !newExpense.amount){
    alert("Both feilds must be filled")
    return;
  }

  setExpenses([
    ...expenses,
    {
      name: newExpense.name,
      amount: Number(newExpense.amount),
    },
  ]);

  setNewExpense({ name: "", amount: "" });
};

  return (
    <div className="budget-page">

      {/* HEADER */}
      <div className="budget-header">
  <h1>Expenses</h1>

  <div style={{ display: "flex", gap: "10px" }}>
    {hasBudget && (
      <>
        <button onClick={() => setEditMode(!editMode)}>
          {editMode ? "Done" : "Edit"}
        </button>

        <button
          onClick={handleRemoveBudget}
          style={{ background: "#dc2626" }}
        >
          Remove Budget
        </button>
      </>
    )}
  </div>
</div>


      {/* =========================
          HAS BUDGET VIEW
      ========================= */}
      {hasBudget ? (
        <>
          {/* TOP CARDS */}
          <div className="budget-grid">
            <div className="budget-card">
              <span>Total Budget</span>

              {editMode ? (
                <input
                  type="number"
                  value={budgetTotal}
                  onChange={handleBudgetChange}
                />
              ) : (
                <h2>${budgetTotal}</h2>
              )}
            </div>

            <div className="budget-card">
              <span>Spent</span>
              <h2>${spent}</h2>
            </div>

            <div className={`budget-card ${remaining < 0 ? "danger" : "good"}`}>
              <span>Remaining</span>
              <h2>${remaining}</h2>
            </div>
          </div>

          {/* EXPENSES */}
          <h2 className="section-title">Expense</h2>

          <div className="budget-list">
            {expenses.map((exp, index) => (
              <div key={index} className="budget-row">
                <span>{exp.name}</span>

                {editMode ? (
                  <input
                    type="number"
                    value={exp.amount}
                    onChange={(e) =>
                      handleExpenseChange(index, e.target.value)
                    }
                  />
                ) : (
                  <span>${exp.amount}</span>
                )}
              </div>
            ))}
          </div>

          {/* 🧮 MATH FOOTER */}
          <div className="budget-footer">
            <div className="math-line">
              <span>Total Spent</span>
              <span>${spent}</span>
            </div>

            <div className="math-line">
              <span>Total Budget</span>
              <span>${budgetTotal}</span>
            </div>

            <div className="math-line">
              <span>- Spent</span>
              <span>-${spent}</span>
            </div>

            <div className="math-line result">
              <span>= Remaining</span>
              <span>${remaining}</span>
            </div>
          </div>
        </>
      ) : (
        /* =========================
           NO BUDGET VIEW
        ========================= */
        <>
          {/* SUMMARY CARD */}
          <div className="budget-card">
            <span>Total Spent</span>
            <h2>${spent}</h2>
          </div>

          {/* EXPENSES */}
          <h2 className="section-title">Expenses</h2>

          <div className="budget-list">
            {expenses.map((exp, index) => (
              <div key={index} className="budget-row">
                <span>{exp.name}</span>

                {editMode ? (
                  <input
                    type="number"
                    value={exp.amount}
                    onChange={(e) =>
                      handleExpenseChange(index, e.target.value)
                    }
                  />
                ) : (
                  <span>${exp.amount}</span>
                )}
                
              </div>
              
            ))}

            <div className="math-line">
              <span>Total Spent</span>
              <span>${spent}</span>
            </div>

          </div>

          {/* ➕ CREATE BUDGET */}
          <div className="budget-create">
            <h3>Add a Budget</h3>

            <input
              type="number"
              placeholder="Enter budget amount"
              value={newBudget}
              onChange={(e) => setNewBudget(e.target.value)}
            />

            <button onClick={handleCreateBudget}>
              Set Budget
            </button>
          </div>
          {/* ➕ ADD EXPENSE */}
<div className="budget-add-expense">
  <h3>➕ Add Expense</h3>

  <input
    type="text"
    placeholder="Expense name"
    value={newExpense.name}
    onChange={(e) =>
      setNewExpense({ ...newExpense, name: e.target.value })
    }
  />

  <input
    type="number"
    placeholder="Amount"
    value={newExpense.amount}
    onChange={(e) =>
      setNewExpense({ ...newExpense, amount: e.target.value })
    }
  />

  <button onClick={handleAddExpense}>Add</button>
</div>
        </>
      )}
    </div>
    
  );
}
