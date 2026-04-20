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
      .then((res) => res.json())
      .then((data) => setTrips(Array.isArray(data) ? data : []))
      .catch((err) => console.error(err));
  }, [token, navigate]);

  return (
    <div className="trips-page">
      <h2 className="trips-title">Your Trips</h2>

      {trips.length === 0 ? (
        <p className="empty-text">No trips found.</p>
      ) : (
        <div className="trips-grid">
          {trips.map((trip) => (
            <div
              key={trip.id}
              className="trip-card"
              onClick={() => navigate(`/trips/${trip.id}`)}
            >
              <h3 className="trip-name">{trip.name}</h3>

             <p className="trip-dates">
                {new Date(trip.startDate).toLocaleDateString("en-US", {
                weekday: "long",
                year: "numeric",
                month: "long",
                day: "numeric",
            })}
            {" → "}
            {new Date(trip.endDate).toLocaleDateString("en-US", {
                weekday: "long",
                year: "numeric",
                month: "long",
                day: "numeric",
            })}
            </p>

            </div>
          ))}
        </div>
      )}
    </div>
  );
}
