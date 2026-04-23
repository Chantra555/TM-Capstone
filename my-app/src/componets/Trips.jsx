import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./trip.css";

export default function Trips() {
  const [trips, setTrips] = useState([]);
  const navigate = useNavigate();

  const token = localStorage.getItem("token");

  useEffect(() => {
    if (!token) {
      navigate("/login");
      return;
    }

    fetch("http://localhost:8081/api/trips", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch trips");
        return res.json();
      })
      .then((data) => {
        setTrips(Array.isArray(data) ? data : []);
      })
      .catch((err) => console.error(err));
  }, [token, navigate]);

  // ✅ DELETE FUNCTION
const handleDelete = (tripId) => {
  if (!window.confirm("Delete this trip?")) return;

  console.log("DELETE TOKEN:", token);

  fetch(`http://localhost:8081/api/trips/${tripId}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  })
    .then((res) => {
      if (!res.ok) {
        return res.text().then((text) => {
          throw new Error(text || "Delete failed");
        });
      }

      setTrips((prev) =>
        prev.filter((t) => (t.id ?? t.tripId) !== tripId)
      );
    })
    .catch((err) => console.error("DELETE ERROR:", err.message));
};


  // ✅ SPLIT TRIPS
const today = new Date();
today.setHours(0, 0, 0, 0); // normalize to start of day

const normalizeDate = (dateStr) => {
  const d = new Date(dateStr);
  d.setHours(0, 0, 0, 0);
  return d;
};

const currentTrips = trips.filter((trip) => {
  if (!trip.endDate) return false;
  return normalizeDate(trip.endDate) >= today;
});

const pastTrips = trips.filter((trip) => {
  if (!trip.endDate) return false;
  return normalizeDate(trip.endDate) < today;
});



  // ✅ CARD RENDER
  const renderTrips = (tripList) => {
    if (tripList.length === 0) {
      return <p className="empty-text">No trips found.</p>;
    }

    return (
      <div className="trips-grid">
        {tripList.map((trip) => {
          const tripId = trip.id ?? trip.tripId;

          return (
            <div key={tripId} className="trip-card">
              <div
                className="trip-click"
                onClick={() => navigate(`/trips/${tripId}`)}
              >
                <h3 className="trip-name">{trip.name}</h3>
                  <p className="trip-dates">
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

              </div>

              {/* ✅ DELETE BUTTON */}
              <button
                className="delete-btn"
                onClick={(e) => {
                  e.stopPropagation(); // prevents card click
                  handleDelete(tripId);
                }}
              >
                Delete
              </button>
            </div>
          );
        })}
      </div>
    );
  };

  return (
  <div className="trips-page">
    <h2 className="trips-title">Your Trips</h2>

    {/* CURRENT TRIPS */}
    {currentTrips.length === 0 ? (
      <p className="empty-text">No active trips.</p>
    ) : (
      renderTrips(currentTrips)
    )}

    {/* PAST TRIPS */}
    {pastTrips.length > 0 && (
      <>
        <h3 className="section-title past">Past Trips</h3>
        {renderTrips(pastTrips)}
      </>
    )}
  </div>
);

}
