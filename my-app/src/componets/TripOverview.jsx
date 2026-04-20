import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

export default function TripOverview() {
  const { id } = useParams();

  const [trip, setTrip] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("token");

    fetch(`http://localhost:8081/api/trips/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then(async (res) => {
        if (!res.ok) throw new Error("Failed to fetch trip");
        return res.json();
      })
      .then((data) => {
        setTrip(data);
      })
      .catch((err) => {
        console.error(err);
        setTrip(null);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [id]);

  // 🧠 helpers
  function formatDate(dateString) {
    const date = new Date(dateString);

    return date.toLocaleDateString("en-US", {
      weekday: "long",
      month: "long",
      day: "numeric",
      year: "numeric",
    });
  }

  function getDuration(start, end) {
    if (!start || !end) return 0;

    const s = new Date(start);
    const e = new Date(end);

    const diff = (e - s) / (1000 * 60 * 60 * 24);

    return Math.round(diff) + 1;
  }

  // 🔄 loading state
  if (loading) {
    return (
      <div style={{ padding: "40px" }}>
        <p>Loading trip...</p>
      </div>
    );
  }

  // ❌ not found state
  if (!trip) {
    return (
      <div style={{ padding: "40px" }}>
        <h2>Trip not found</h2>
      </div>
    );
  }

  // ✅ main UI
  return (
    <div className="trip-overview">
      {/* HEADER */}
      <div className="trip-header">
        <h1>{trip.name}</h1>

        <p>
          {formatDate(trip.startDate)} → {formatDate(trip.endDate)}
        </p>
      </div>

      {/* INFO CARDS */}
      <div className="trip-info-grid">
        <div className="info-card">
          <span>Destination</span>
          <h3>{trip.location}</h3>
        </div>

        <div className="info-card">
          <span>Budget</span>
          <h3>${trip.idealBudget}</h3>
        </div>

        <div className="info-card">
          <span>Duration</span>
          <h3>{getDuration(trip.startDate, trip.endDate)} days</h3>
        </div>
      </div>
    </div>
  );
}
