import React, { useState } from "react";
import axios from "axios";
import "./lodging.css";
import { useParams, useNavigate } from "react-router-dom";

export default function AddLodgingPage() {
  const { tripId } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    name: "",
    location: "",
    checkIn: "",
    checkOut: "",
    price: "",
    notes: "",
  });

  const [successMsg, setSuccessMsg] = useState("");

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const token = localStorage.getItem("token");

      await axios.post(
        "http://localhost:8081/api/lodgings",
        {
          ...form,
          price: parseFloat(form.price),
          tripId: Number(tripId),
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      // ✅ success message
      setSuccessMsg("Lodging saved successfully!");

      // 🔥 clear form so user can add another
      setForm({
        name: "",
        location: "",
        checkIn: "",
        checkOut: "",
        price: "",
        notes: "",
      });

      // optional: remove message after 3 seconds
      setTimeout(() => setSuccessMsg(""), 3000);

    } catch (err) {
      console.error(err);
      alert("Failed to save lodging");
    }
  };

  return (
    <div className="lodging-page">
      <div className="lodging-card">
        <h1>Add Lodging</h1>

        {/* ✅ success message */}
        {successMsg && <p className="success-msg">{successMsg}</p>}

        <form onSubmit={handleSubmit} className="lodging-form">

          <div className="grid">
            <input
              name="name"
              placeholder="Hotel / Airbnb name"
              value={form.name}
              onChange={handleChange}
            />

            <input
              name="location"
              placeholder="Location (city, address)"
              value={form.location}
              onChange={handleChange}
            />
          </div>

          <div className="grid">
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
          </div>

          <input
            name="price"
            placeholder="Cost per night"
            value={form.price}
            onChange={handleChange}
          />

          <textarea
            name="notes"
            placeholder="Notes (wifi, breakfast, cancellation policy...)"
            value={form.notes}
            onChange={handleChange}
          />

          <div className="actions">
            <button
              type="button"
              className="secondary"
              onClick={() => navigate(-1)}
            >
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
