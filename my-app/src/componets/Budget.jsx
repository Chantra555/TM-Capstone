import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import "./budget.css";

export default function Budget() {
  const { tripId } = useParams();

  const [budget, setBudget] = useState(null);
  const [expenses, setExpenses] = useState([]);

  const [newBudget, setNewBudget] = useState("");
  const [newExpense, setNewExpense] = useState({
    name: "",
    amount: "",
  });

  const [editingIndex, setEditingIndex] = useState(null);

  /* ================= JWT FETCH ================= */

  const apiFetch = async (url, options = {}) => {
    const token = localStorage.getItem("token");

    const res = await fetch(url, {
      ...options,
      headers: {
        "Content-Type": "application/json",
        Authorization: token ? `Bearer ${token}` : "",
        ...options.headers,
      },
    });

    return res;
  };

  /* ================= LOAD DATA ================= */

  useEffect(() => {
    if (tripId) {
      fetchBudget();
      fetchExpenses();
    }
  }, [tripId]);

  const fetchBudget = async () => {
    try {
      const res = await apiFetch(
        `http://localhost:8081/api/budget/trip/${tripId}`
      );

      if (!res.ok) {
        setBudget(null);
        return;
      }

      const data = await res.json();
      setBudget(data);
    } catch (err) {
      console.error("Budget load failed:", err);
    }
  };

  const fetchExpenses = async () => {
    try {
      const res = await apiFetch(
        `http://localhost:8081/api/expense/trip/${tripId}`
      );

      if (!res.ok) {
        setExpenses([]);
        return;
      }

      const data = await res.json();
      setExpenses(data || []);
    } catch (err) {
      console.error("Expense load failed:", err);
    }
  };

  /* ================= FIXED TOTAL ================= */

  const spent = expenses.reduce((sum, e) => {
    return sum + Number(e.cost);
  }, 0);

  const remaining = budget?.idealBudget
    ? budget.idealBudget - spent
    : null;

  const hasBudget = budget !== null;

  const formatMoney = (num) =>
    Number(num || 0).toLocaleString(undefined, {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    });

  /* ================= BUDGET ================= */

  const handleCreateBudget = async () => {
    if (!newBudget) return alert("Enter budget");

    await apiFetch(`http://localhost:8081/api/budget/${tripId}`, {
      method: "POST",
      body: JSON.stringify({
        idealBudget: Number(newBudget),
      }),
    });

    setNewBudget("");
    fetchBudget();
  };

  /* ================= EXPENSES ================= */

  const handleAddExpense = async () => {
    if (!newExpense.name || !newExpense.amount) {
      return alert("Fill both fields");
    }

    await apiFetch("http://localhost:8081/api/expense", {
      method: "POST",
      body: JSON.stringify({
        name: newExpense.name,
        cost: Number(newExpense.amount),
        tripId: Number(tripId),
      }),
    });

    setNewExpense({ name: "", amount: "" });
    fetchExpenses();
  };

  const handleDeleteExpense = async (id) => {
    await apiFetch(`http://localhost:8081/api/expense/${id}`, {
      method: "DELETE",
    });

    fetchExpenses();
  };

  const handleUpdateExpense = async (exp) => {
    await apiFetch(`http://localhost:8081/api/expense/${exp.id}`, {
      method: "PUT",
      body: JSON.stringify({
        name: exp.name,
        cost: exp.cost,
        tripId: Number(tripId),
      }),
    });

    setEditingIndex(null);
    fetchExpenses();
  };

  /* ================= UI ================= */

  return (
    <div className="budget-page">

      {/* HEADER */}
      <div className="budget-header">
        <h1>Budget & Expenses</h1>
      </div>

      {/* SUMMARY */}
      <div className="summary-grid">
        <div className="card">
          <p>Total Spent</p>
          <h2>${formatMoney(spent)}</h2>
        </div>

        {hasBudget && (
          <>
            <div className="card">
              <p>Total Budget</p>
              <h2>${formatMoney(budget.idealBudget)}</h2>
            </div>

            <div className={`card ${remaining < 0 ? "negative" : "positive"}`}>
              <p>Remaining</p>
              <h2>${formatMoney(remaining)}</h2>
            </div>
          </>
        )}
      </div>

      {/* EXPENSES LIST */}
      <div className="expenses">
        <h2>Expenses</h2>

        {expenses.length === 0 && (
          <p className="empty">No expenses yet</p>
        )}

        {expenses.map((exp, index) => {
          const isEditing = editingIndex === index;

          return (
            <div key={exp.id || index} className="expense-row">

              {isEditing ? (
                <>
                  <input
                    value={exp.name}
                    onChange={(e) => {
                      const updated = [...expenses];
                      updated[index].name = e.target.value;
                      setExpenses(updated);
                    }}
                  />

                  <input
                    type="number"
                    value={exp.cost}
                    onChange={(e) => {
                      const updated = [...expenses];
                      updated[index].cost = Number(e.target.value);
                      setExpenses(updated);
                    }}
                  />

                  <button onClick={() => handleUpdateExpense(exp)}>
                    Save
                  </button>
                </>
              ) : (
                <>
                  <span>{exp.name}</span>
                  <span className="amount">
                    ${formatMoney(exp.cost)}
                  </span>

                  <div className="row-actions">
                    <button onClick={() => setEditingIndex(index)}>
                      Edit
                    </button>

                    <button
                      className="delete-btn"
                      onClick={() => handleDeleteExpense(exp.id)}
                    >
                      Delete
                    </button>
                  </div>
                </>
              )}
            </div>
          );
        })}
      </div>

      {/* ADD EXPENSE */}
      <div className="card">
        <h3>Add Expense</h3>

        <input
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

        <button onClick={handleAddExpense}>
          Add Expense
        </button>
      </div>

      {/* CREATE BUDGET */}
      {!hasBudget && (
        <div className="card">
          <h3>Create Budget</h3>

          <input
            type="number"
            placeholder="Enter budget"
            value={newBudget}
            onChange={(e) => setNewBudget(e.target.value)}
          />

          <button onClick={handleCreateBudget}>
            Set Budget
          </button>
        </div>
      )}

    </div>
  );
}
