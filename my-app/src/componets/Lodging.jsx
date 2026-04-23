import React, { useState } from "react";
import "./lodging.css";

export default function AddLodgingPage() {
  const [form, setForm] = useState({
    name: "",
    location: "",
    checkIn: "",
    checkOut: "",
    price: "",
    notes: "",
  });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Lodging Submitted:", form);
    alert("Lodging saved (mock)");
  };

  return (
    <div className="lodging-page">
      <div className="lodging-card">
        <h1>Add Lodging</h1>
        <p className="subtitle">Save hotel, Airbnb, or stay details for your trip</p>

        <form onSubmit={handleSubmit} className="lodging-form">
          <div className="grid">
            <input
              name="name"
              placeholder="Hotel / Place name"
              value={form.name}
              onChange={handleChange}
            />

            <input
              name="location"
              placeholder="Location (city, address)"
              value={form.location}
              onChange={handleChange}
            />

            <input
              type="date"
              name="checkIn"
              value={form.checkIn}
              onChange={handleChange}
            />

            <input
              type="date"
              name="checkOut"
              value={form.checkOut}
              onChange={handleChange}
            />

            <input
              name="price"
              placeholder="Cost per night"
              value={form.price}
              onChange={handleChange}
            />
          </div>

          <textarea
            name="notes"
            placeholder="Notes (wifi, breakfast, cancellation policy...)"
            value={form.notes}
            onChange={handleChange}
          />

          <div className="actions">
            <button type="button" className="secondary">
              Cancel
            </button>
            <button type="submit" className="primary">
              Save Lodging
             </button>
          </div>
        </form>
      </div>
    </div>
  );
}