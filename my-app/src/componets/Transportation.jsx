import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import "./transportation.css";

export default function Transportation() {
  const { tripId } = useParams();

  const API_BASE = "http://localhost:8081";

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

  const token = localStorage.getItem("token");

  // 📥 GET ALL TRANSPORTATION
  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await fetch(
          `${API_BASE}/api/transportation/trip/${tripId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (!res.ok) {
          console.error("Failed to fetch transportation");
          return;
        }

        const data = await res.json();
        setItems(data);
      } catch (err) {
        console.error("Network error:", err);
      }
    };

    fetchData();
  }, [tripId]);

  // ✏️ FORM CHANGE
  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  // ➕ CREATE / ✏️ UPDATE
  const handleSubmit = async (e) => {
    e.preventDefault();

    const isEditing = Boolean(editingId);

    const url = isEditing
      ? `${API_BASE}/api/transportation/${editingId}`
      : `${API_BASE}/api/transportation/trip/${tripId}`;

    const method = isEditing ? "PUT" : "POST";

    try {
      const res = await fetch(url, {
        method,
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(form),
      });

      if (!res.ok) {
        console.error("Save failed");
        return;
      }

      const data = await res.json();

      if (isEditing) {
        setItems((prev) =>
          prev.map((item) => (item.id === editingId ? data : item))
        );
        setEditingId(null);
      } else {
        setItems((prev) => [...prev, data]);
      }

      // reset form
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
    } catch (err) {
      console.error("Submit error:", err);
    }
  };

  // ✏️ EDIT
  const handleEdit = (item) => {
    const { id, ...rest } = item;
    setForm(rest);
    setEditingId(id);
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  // ❌ DELETE
  const removeItem = async (id) => {
    try {
      await fetch(`${API_BASE}/api/transportation/${id}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      setItems((prev) => prev.filter((item) => item.id !== id));

      if (editingId === id) {
        setEditingId(null);
      }
    } catch (err) {
      console.error("Delete error:", err);
    }
  };

  return (
    <div className="transport-page">

      <p>Transportation</p>

      {/* FORM */}
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
            placeholder="Airline / Transport"
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

      {/* LIST */}
      <div className="transport-list">
        {items.map((item) => (
          <div key={item.id} className="transport-card">

            <div className="card-header">
              <h3>{item.title || item.type.toUpperCase()}</h3>

              <div className="card-actions">
                <button onClick={() => handleEdit(item)}>✏️</button>
                <button onClick={() => removeItem(item.id)}>✕</button>
              </div>
            </div>

            <p>
              <strong>{item.departure}</strong> → <strong>{item.arrival}</strong>
            </p>

            <p>
              {item.departTime &&
                new Date(item.departTime).toLocaleString()}{" "}
              →{" "}
              {item.arriveTime &&
                new Date(item.arriveTime).toLocaleString()}
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
