import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import "./tripOverview.css";

export default function TripOverview() {
  const { tripId } = useParams();

  const [trip, setTrip] = useState(null);
  const [membersData, setMembersData] = useState(null);

  const [expenses, setExpenses] = useState([]);
  const [itinerary, setItinerary] = useState([]);
  const [lodgings, setLodgings] = useState([]);

  const [loading, setLoading] = useState(true);

  const [isEditing, setIsEditing] = useState(false);

  const [editingLodgingId, setEditingLodgingId] = useState(null);
  const [lodgingForm, setLodgingForm] = useState({});

  const [editForm, setEditForm] = useState({
    location: "",
    startDate: "",
    endDate: "",
  });

  /* ================= FETCH ================= */
  useEffect(() => {
    if (!tripId) return;

    const loadData = async () => {
      try {
        setLoading(true);

        const token = localStorage.getItem("token");

        const headers = {
          "Content-Type": "application/json",
          ...(token && { Authorization: `Bearer ${token}` }),
        };

        const [tripRes, expenseRes, itineraryRes, membersRes, lodgingRes] =
          await Promise.all([
            fetch(`http://localhost:8081/api/trips/${tripId}`, { headers }),
            fetch(`http://localhost:8081/api/expense/trip/${tripId}`, { headers }),
            fetch(`http://localhost:8081/api/itinerary/trip/${tripId}`, { headers }),
            fetch(`http://localhost:8081/api/trips/${tripId}/members`, { headers }),
            fetch(`http://localhost:8081/api/lodgings/trip/${tripId}`, { headers }),
          ]);

        if (!tripRes.ok) throw new Error("Failed to load trip");

        const tripData = await tripRes.json();
        const expenseData = await expenseRes.json();
        const itineraryData = await itineraryRes.json();
        const membersData = await membersRes.json();
        const lodgingData = await lodgingRes.json();

        setTrip(tripData);
        setExpenses(Array.isArray(expenseData) ? expenseData : []);
        setItinerary(Array.isArray(itineraryData) ? itineraryData : []);
        setMembersData(membersData);
        setLodgings(Array.isArray(lodgingData) ? lodgingData : []);

        setEditForm({
          location: tripData.location || "",
          startDate: tripData.startDate || "",
          endDate: tripData.endDate || "",
        });

      } catch (err) {
        console.error(err);
        setTrip(null);
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, [tripId]);

  /* ================= TRIP EDIT ================= */
  const handleChange = (e) => {
    setEditForm({
      ...editForm,
      [e.target.name]: e.target.value,
    });
  };

  const handleSave = async () => {
    try {
      const token = localStorage.getItem("token");

      const res = await fetch(
        `http://localhost:8081/api/trips/${tripId}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(editForm),
        }
      );

      if (!res.ok) throw new Error("Update failed");

      const updated = await res.json();
      setTrip(updated);
      setIsEditing(false);
    } catch (err) {
      console.error(err);
    }
  };

  /* ================= LODGING ================= */
  const handleLodgingChange = (e) => {
    setLodgingForm({
      ...lodgingForm,
      [e.target.name]: e.target.value,
    });
  };

  const startEditLodging = (lodging) => {
    setEditingLodgingId(lodging.id);
    setLodgingForm(lodging);
  };

  const saveLodging = async () => {
    try {
      const token = localStorage.getItem("token");

      await fetch(`http://localhost:8081/api/lodgings/${editingLodgingId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(lodgingForm),
      });

      setLodgings((prev) =>
        prev.map((l) =>
          l.id === editingLodgingId ? { ...lodgingForm } : l
        )
      );

      setEditingLodgingId(null);
    } catch (err) {
      console.error(err);
    }
  };

  const deleteLodging = async (id) => {
    try {
      const token = localStorage.getItem("token");

      await fetch(`http://localhost:8081/api/lodgings/${id}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      setLodgings((prev) => prev.filter((l) => l.id !== id));
    } catch (err) {
      console.error(err);
    }
  };

  /* ================= HELPERS ================= */
  function formatDate(dateString) {
    return new Date(dateString).toLocaleDateString("en-US", {
      weekday: "long",
      month: "long",
      day: "numeric",
      year: "numeric",
    });
  }

  function getDuration(start, end) {
    if (!start || !end) return 0;
    return (
      Math.round(
        (new Date(end) - new Date(start)) / (1000 * 60 * 60 * 24)
      ) + 1
    );
  }

  const totalSpent = expenses.reduce(
    (sum, e) => sum + (Number(e.cost) || 0),
    0
  );

  /* ================= STATES ================= */
  if (loading) return <div className="loading">Loading trip...</div>;
  if (!trip) return <div className="loading">Trip not found</div>;

  /* ================= RENDER ================= */
  return (
    <div className="trip-overview">

      {/* HEADER */}
      <div className="trip-header">
        <div>
          <h1>{trip.name}</h1>

          {membersData && (
            <p className="trip-members-preview">
              👑 {membersData.owner.username} • 👥{" "}
              {membersData.members.length} members
            </p>
          )}
        </div>

        <div style={{ marginTop: "10px" }}>
          {isEditing ? (
            <>
              <button className="btn-save" onClick={handleSave}>
                Save
              </button>
              <button
                className="btn-cancel"
                onClick={() => setIsEditing(false)}
              >
                Cancel
              </button>
            </>
          ) : (
            <button
              className="btn-edit"
              onClick={() => setIsEditing(true)}
            >
              Edit Trip
            </button>
          )}
        </div>
      </div>

      {/* INFO CARDS */}
      <div className="trip-info-grid">

        <div className="info-card">
          <span>Destination</span>
          {isEditing ? (
            <input
              name="location"
              value={editForm.location}
              onChange={handleChange}
            />
          ) : (
            <h3>{trip.location}</h3>
          )}
        </div>

        <div className="info-card">
          <span>Start Date</span>
          {isEditing ? (
            <input
              type="date"
              name="startDate"
              value={editForm.startDate}
              onChange={handleChange}
            />
          ) : (
            <h3>{formatDate(trip.startDate)}</h3>
          )}
        </div>

        <div className="info-card">
          <span>End Date</span>
          {isEditing ? (
            <input
              type="date"
              name="endDate"
              value={editForm.endDate}
              onChange={handleChange}
            />
          ) : (
            <h3>{formatDate(trip.endDate)}</h3>
          )}
        </div>

        <div className="info-card">
          <span>Duration</span>
          <h3>{getDuration(trip.startDate, trip.endDate)} days</h3>
        </div>

        <div className="info-card">
          <span>Total Spent</span>
          <h3>${totalSpent.toFixed(2)}</h3>
        </div>

      </div>

      {/* ITINERARY */}
      <div className="section">
        <h2>Events</h2>

        <div className="card-grid">
          {itinerary.length === 0 ? (
            <p className="empty">No itinerary items yet.</p>
          ) : (
            itinerary.map((item) => (
              <div className="card" key={item.id}>
                <div className="card-title">{item.title}</div>
                <p className="card-text">{item.description}</p>
              </div>
            ))
          )}
        </div>
      </div>

      {/* EXPENSES */}
      <div className="section">
        <h2>Expenses</h2>

        <div className="card-grid">
          {expenses.length === 0 ? (
            <p className="empty">No expenses yet.</p>
          ) : (
            expenses.map((expense) => (
              <div className="card" key={expense.id}>
                <div className="card-title">{expense.name}</div>
                <p className="card-text">${expense.cost}</p>
              </div>
            ))
          )}
        </div>
      </div>

      {/* Lodding */}
      <div className="section">
        <h2>Lodging</h2>

        <div className="card-grid">
          {lodgings.length === 0 ? (
            <p className="empty">No lodging yet.</p>
          ) : (
            lodgings.map((l) => (
              <div className="card" key={l.id}>
                {editingLodgingId === l.id ? (
                  <>
                    <input name="name" value={lodgingForm.name || ""} onChange={handleLodgingChange} />
                    <input name="location" value={lodgingForm.location || ""} onChange={handleLodgingChange} />
                    <input type="date" name="checkIn" value={lodgingForm.checkIn || ""} onChange={handleLodgingChange} />
                    <input type="date" name="checkOut" value={lodgingForm.checkOut || ""} onChange={handleLodgingChange} />
                    <input name="price" value={lodgingForm.price || ""} onChange={handleLodgingChange} />
                    <textarea name="notes" value={lodgingForm.notes || ""} onChange={handleLodgingChange} />

                    <button onClick={saveLodging}>Save</button>
                    <button onClick={() => setEditingLodgingId(null)}>Cancel</button>
                  </>
                ) : (
                  <>
                    <div className="card-title">{l.name}</div>
                    <p className="card-text">{l.location}</p>
                    <p className="card-text">{l.checkIn} → {l.checkOut}</p>
                    <p className="card-text">${l.price}</p>
                    <p className="card-text">{l.notes}</p>

                    <button onClick={() => startEditLodging(l)}>Edit</button>
                    <button onClick={() => deleteLodging(l.id)}>Delete</button>
                  </>
                )}
              </div>
            ))
          )}
        </div>
      </div>

    </div>
  );
}
