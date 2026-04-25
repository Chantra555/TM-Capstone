import React, { useState } from "react";
import "./transportation.css";

export default function Transportation() {
  const [form, setForm] = useState({
    type: "flight",
    title: "",
    departure: "",
    arrival: "",
    departTime: "",
    arriveTime: "",
    carrier: "",
    confirmation: "",
    notes: "",
  });

  const [items, setItems] = useState([]);
  const [editingId, setEditingId] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (editingId) {
      setItems(items.map((item) => (item.id === editingId ? { ...form, id: editingId } : item)));
      setEditingId(null);
    } else {
      setItems([...items, { ...form, id: Date.now() }]);
    }

    setForm({
      type: "flight",
      title: "",
      departure: "",
      arrival: "",
      departTime: "",
      arriveTime: "",
      carrier: "",
      confirmation: "",
      notes: "",
    });
  };

  const handleEdit = (item) => {
    setForm(item);
    setEditingId(item.id);
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const removeItem = (id) => {
    setItems(items.filter((item) => item.id !== id));
    if (editingId === id) setEditingId(null);
  };

  return (
    <div className="transport-page">
      <p>Transportation</p>

      <form className="transport-form" onSubmit={handleSubmit}>
        <div className="row">
          <select name="type" value={form.type} onChange={handleChange}>
            <option value="flight">Flight</option>
            <option value="roadtrip">Road Trip</option>
            <option value="train">Train</option>
          </select>

          <input
            name="title"
            placeholder="Trip Title"
            value={form.title}
            onChange={handleChange}
          />
        </div>

        <div className="row">
          <input
            name="departure"
            placeholder="Departure"
            value={form.departure}
            onChange={handleChange}
          />

          <input
            name="arrival"
            placeholder="Arrival"
            value={form.arrival}
            onChange={handleChange}
          />
        </div>

        <div className="row">
          <input
            type="datetime-local"
            name="departTime"
            value={form.departTime}
            onChange={handleChange}
          />

          <input
            type="datetime-local"
            name="arriveTime"
            value={form.arriveTime}
            onChange={handleChange}
          />
        </div>

        <div className="row">
          <input
            name="carrier"
            placeholder="Airline/Train System"
            value={form.carrier}
            onChange={handleChange}
          />

          <input
            name="confirmation"
            placeholder="Confirmation #"
            value={form.confirmation}
            onChange={handleChange}
          />
        </div>

        <textarea
          name="notes"
          placeholder="Notes"
          value={form.notes}
          onChange={handleChange}
        />

        <button type="submit">
          {editingId ? "Update Transportation" : "Add Transportation"}
        </button>
      </form>

      <div className="transport-list">
        {items.map((item) => (
          <div key={item.id} className="transport-card">
            <div className="card-header">
              <h3>{item.title || item.type.toUpperCase()}</h3>
              <div>
                <button onClick={() => handleEdit(item)}>✏️</button>
                <button onClick={() => removeItem(item.id)}>✕</button>
              </div>
            </div>

            <p>
              <strong>{item.departure}</strong> → <strong>{item.arrival}</strong>
            </p>

            <p>
              {item.departTime && new Date(item.departTime).toLocaleString()} →{" "}
              {item.arriveTime && new Date(item.arriveTime).toLocaleString()}
            </p>

            {item.carrier && <p>Carrier: {item.carrier}</p>}
            {item.confirmation && <p>Confirmation: {item.confirmation}</p>}
            {item.notes && <p className="notes">{item.notes}</p>}
          </div>
        ))}
      </div>
    </div>
  );
}