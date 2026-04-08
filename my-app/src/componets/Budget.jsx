import { useState, useEffect } from "react";

export default function Budget({ trip, setTrip }) {
  // Initial categories
  const initialRows = [
    { item: "Lodging", cost: 0 },
    { item: "Food", cost: 0 },
    { item: "Flights", cost: 0 },
  ];

  const [rows, setRows] = useState(initialRows);
  const [total, setTotal] = useState(0);

  // Update total whenever rows change
  useEffect(() => {
    const sum = rows.reduce((acc, row) => acc + Number(row.cost || 0), 0);
    setTotal(sum);
    // Optional: update trip state
    setTrip({ ...trip, budget: sum });
  }, [rows]);

  // Handle input changes
  const handleChange = (index, field, value) => {
    const newRows = [...rows];
    if (field === "cost") value = Number(value); // convert to number
    newRows[index][field] = value;
    setRows(newRows);
  };

  // Add a new category row
  const addRow = () => {
    setRows([...rows, { item: "", cost: 0 }]);
  };

  return (
    <div className="budget-container">
      <h2>Budget</h2>
      <table className="budget-table">
        <thead>
          <tr>
            <th>Item</th>
            <th>Cost ($)</th>
          </tr>
        </thead>
        <tbody>
          {rows.map((row, i) => (
            <tr key={i}>
              <td>
                <input
                  type="text"
                  value={row.item}
                  onChange={(e) => handleChange(i, "item", e.target.value)}
                  placeholder="Category"
                />
              </td>
              <td>
                <input
                  type="number"
                  value={row.cost}
                  onChange={(e) => handleChange(i, "cost", e.target.value)}
                  placeholder="0"
                  min="0"
                />
              </td>
            </tr>
          ))}
        </tbody>
        <tfoot>
          <tr>
            <td>Total</td>
            <td>{total}</td>
          </tr>
        </tfoot>
      </table>
      <button onClick={addRow}>Add Category</button>
    </div>
  );
}
