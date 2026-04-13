import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import "./createTrip.css";

export default function CreateTrip() {
  const navigate = useNavigate();

  const [showForm, setShowForm] = useState(false);
  const [tripCreated, setTripCreated] = useState(false);

  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);

  const [trip, setTrip] = useState({
    name: "",
    location: "",
    idealBudget: "",
  });

  const isLoggedIn = localStorage.getItem("token");

  // Handle text inputs
  const handleChange = (e) => {
    const { name, value } = e.target;
    setTrip({ ...trip, [name]: value });
  };

  // Format date for backend (YYYY-MM-DD)
  const formatDate = (date) => {
    return date ? date.toISOString().split("T")[0] : null;
  };

  // Handle start date change
  const handleStartDateChange = (date) => {
    setStartDate(date);

    if (endDate && date > endDate) {
      setEndDate(null);
    }
  };

  // Save trip to backend
  const handleSave = async () => {
    if (!isLoggedIn) {
      alert("Please login to save your trip");
      navigate("/login");
      return;
    }

    if (!trip.name) {
      alert("Trip name must not be empty");
      return;
    }

    if (!startDate || !endDate) {
      alert("Please select both start and end dates");
      return;
    }

    if (endDate < startDate) {
      alert("End date cannot be before start date");
      return;
    }

    const tripData = {
      ...trip,
      startDate: formatDate(startDate),
      endDate: formatDate(endDate),
    };

    try {
      const response = await fetch("http://localhost:8081/api/trips", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        body: JSON.stringify(tripData),
      });

      if (!response.ok) {
        throw new Error("Failed to save trip");
      }

      const data = await response.text();
      console.log("Saved trip:", data);

      setTripCreated(true);
    } catch (error) {
      console.error(error);
      alert("Error saving trip");
    }
  };

  return (
    <div className="create-trip-page">
      {/* + New Trip Card */}
      {!showForm && !tripCreated && (
        <div
          className="new-trip-card"
          onClick={() => setShowForm(true)}
        >
          + New Trip
        </div>
      )}

      {/* Trip Form */}
      {showForm && !tripCreated && (
        <div className="trip-form-card">
          <h2>Create Trip</h2>

          <input
            type="text"
            name="name"
            placeholder="Trip Name"
            value={trip.name}
            onChange={handleChange}
          />

          <input
            type="text"
            name="location"
            placeholder="Location Name"
            value={trip.location}
            onChange={handleChange}
          />

          <DatePicker
            selected={startDate}
            onChange={handleStartDateChange}
            placeholderText="Start Date"
            className="input-field"
          />

          <DatePicker
            selected={endDate}
            onChange={(date) => setEndDate(date)}
            placeholderText="End Date"
            className="input-field"
          />

          <input
            type="number"
            name="idealBudget"
            placeholder="Ideal Budget"
            value={trip.idealBudget}
            onChange={handleChange}
          />

          {isLoggedIn ? (
            <button onClick={handleSave}>Save Trip</button>
          ) : (
            <div className="login-link">
              <Link
                to="/login"
                style={{ color: "red", marginBottom: "0.5rem" }}
              >
                login required to save trip
              </Link>
            </div>
          )}
        </div>
      )}

      {/* Confirmation */}
      {tripCreated && (
        <div
          className="trip-created-card"
          onClick={() => {
            setTripCreated(false);
            setShowForm(false);

            // Reset form
            setTrip({
              name: "",
              location: "",
              idealBudget: "",
            });
            setStartDate(null);
            setEndDate(null);
          }}
          style={{ cursor: "pointer" }}
        >
          <h2>🎉 Created New Trip!</h2>
          <p>Click to create another trip</p>
        </div>
      )}
    </div>
  );
}
