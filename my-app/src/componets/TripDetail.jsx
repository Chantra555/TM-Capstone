import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

export default function TripDetail() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [trip, setTrip] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (!token) {
      navigate("/login");
      return;
    }

    fetch(`http://localhost:8081/api/trips/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then(async (res) => {
        if (!res.ok) {
          throw new Error("Failed to fetch trip");
        }
        return res.json();
      })
      .then((data) => {
        console.log("TRIP DETAIL:", data);
        setTrip(data);
      })
      .catch((err) => {
        console.error(err);
        setTrip(null);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [id, navigate]);

  if (loading) {
    return (
      <div style={{ padding: "40px" }}>
        <p>Loading trip...</p>
      </div>
    );
  }

  if (!trip) {
    return (
      <div style={{ padding: "40px" }}>
        <p>Trip not found.</p>
        <button onClick={() => navigate("/trips")}>
          Go back
        </button>
      </div>
    );
  }

  return (
    <div style={{ padding: "40px", maxWidth: "800px", margin: "0 auto" }}>
      {/* HEADER */}
      <h1 style={{ fontSize: "32px", marginBottom: "10px" }}>
        {trip.name}
      </h1>

      <p style={{ color: "#6b7280", marginBottom: "20px" }}>
        {trip.startDate} → {trip.endDate}
      </p>

      {/* DETAILS CARD */}
      <div
        style={{
          background: "white",
          border: "1px solid #e5e7eb",
          borderRadius: "12px",
          padding: "20px",
        }}
      >
        <p><strong>Location:</strong> {trip.location}</p>
        <p><strong>Budget:</strong> ${trip.idealBudget}</p>
      </div>

      {/* BACK BUTTON */}
      <button
        onClick={() => navigate("/trips")}
        style={{
          marginTop: "20px",
          padding: "10px 14px",
          border: "none",
          borderRadius: "8px",
          cursor: "pointer",
          background: "#111827",
          color: "white",
        }}
      >
        ← Back to Trips
      </button>
    </div>
  );
}
