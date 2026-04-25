import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./trip.css";

export default function Trips() {
  const [trips, setTrips] = useState([]);
  const [loading, setLoading] = useState(true);

  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  /* ================= FETCH TRIPS ================= */
  useEffect(() => {
    if (!token) {
      navigate("/login");
      return;
    }

    const fetchTrips = async () => {
      try {
        setLoading(true);

        const res = await fetch("http://localhost:8081/api/trips", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (!res.ok) throw new Error("Failed to fetch trips");

        const data = await res.json();
        const baseTrips = Array.isArray(data) ? data : [];

        // 🔥 ENRICH TRIPS WITH MEMBER COUNTS
        const enrichedTrips = await Promise.all(
          baseTrips.map(async (trip) => {
            try {
              const membersRes = await fetch(
                `http://localhost:8081/api/trips/${trip.id}/members`,
                {
                  headers: {
                    Authorization: `Bearer ${token}`,
                  },
                }
              );

              if (!membersRes.ok) throw new Error();

              const membersData = await membersRes.json();

              return {
                ...trip,
                membersCount: membersData.members?.length || 0,
                owner: membersData.owner || trip.owner,
              };
            } catch {
              return {
                ...trip,
                membersCount: 0,
              };
            }
          })
        );

        setTrips(enrichedTrips);
      } catch (err) {
        console.error("FETCH ERROR:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchTrips();
  }, [token, navigate]);

  /* ================= DELETE TRIP ================= */
  const handleDelete = async (tripId) => {
    if (!window.confirm("Delete this trip?")) return;

    try {
      const res = await fetch(
        `http://localhost:8081/api/trips/${tripId}`,
        {
          method: "DELETE",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!res.ok) throw new Error("Delete failed");

      setTrips((prev) => prev.filter((t) => t.id !== tripId));
    } catch (err) {
      console.error("DELETE ERROR:", err);
    }
  };

  /* ================= DATE HELPERS ================= */
  const today = new Date();
  today.setHours(0, 0, 0, 0);

  const normalizeDate = (dateStr) => {
    const d = new Date(dateStr);
    d.setHours(0, 0, 0, 0);
    return d;
  };

  const currentTrips = trips.filter(
    (trip) => trip.endDate && normalizeDate(trip.endDate) >= today
  );

  const pastTrips = trips.filter(
    (trip) => trip.endDate && normalizeDate(trip.endDate) < today
  );

  /* ================= RENDER ================= */
  const renderTrips = (tripList) => {
    if (!tripList.length) {
      return <p className="empty-text">No trips found.</p>;
    }

    return (
      <div className="trips-grid">
        {tripList.map((trip) => (
          <div key={trip.id} className="trip-card">

            <div
              className="trip-click"
              onClick={() => navigate(`/trips/${trip.id}`)}
            >
              <h3 className="trip-name">{trip.name}</h3>

              <p className="trip-owner">
                👑 {trip.owner?.username || "Unknown"}
              </p>

              <p className="trip-datez">
                {trip.startDate &&
                  new Date(trip.startDate).toLocaleDateString("en-US", {
                    weekday: "long",
                    year: "numeric",
                    month: "long",
                    day: "numeric",
                  })}

                {" → "}

                {trip.endDate &&
                  new Date(trip.endDate).toLocaleDateString("en-US", {
                    weekday: "long",
                    year: "numeric",
                    month: "long",
                    day: "numeric",
                  })}
              </p>

              <p className="trip-members">
                👥 {trip.membersCount ?? 0} members
              </p>
            </div>

            <button
              className="delete-btn"
              onClick={(e) => {
                e.stopPropagation();
                handleDelete(trip.id);
              }}
            >
              Delete
            </button>
          </div>
        ))}
      </div>
    );
  };

  if (loading) return <div className="loading">Loading trips...</div>;

  return (
    <div className="trips-page">
      <h2 className="trips-title">Your Trips</h2>

      {currentTrips.length > 0 ? (
        renderTrips(currentTrips)
      ) : (
        <p className="empty-text">No active trips.</p>
      )}

      {pastTrips.length > 0 && (
        <>
          <h3 className="section-title past">Past Trips</h3>
          {renderTrips(pastTrips)}
        </>
      )}
    </div>
  );
}
