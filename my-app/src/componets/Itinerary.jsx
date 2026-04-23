import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import {
  DragDropContext,
  Droppable,
  Draggable
} from "@hello-pangea/dnd";
import "./itinerary.css";

export default function Itinerary() {
  const { tripId } = useParams();

  const [events, setEvents] = useState([]);
  const [editing, setEditing] = useState(null);
  const [trip, setTrip] = useState(null);


  const [form, setForm] = useState({
    title: "",
    date: "",
    time: "",
    type: "activity",
    customType: "",
    description: ""
  });

  const token = localStorage.getItem("token");

  const headers = {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` })
  };

  /* ================= FETCH ================= */
  useEffect(() => {
    if (!tripId) return;

    fetch(`http://localhost:8081/api/itinerary/trip/${tripId}`, {
      headers
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed fetch");
        return res.json();
      })
      .then((data) => {
        setEvents(Array.isArray(data) ? data : []);
      })
      .catch((err) => console.error(err));
  }, [tripId]);

useEffect(() => {
  if (!tripId) return;

  fetch(`http://localhost:8081/api/trips/${tripId}`, {
    headers
  })
    .then((res) => {
      if (!res.ok) throw new Error("Failed to fetch trip");
      return res.json();
    })
    .then((data) => {
      setTrip(data);
    })
    .catch((err) => console.error(err));
}, [tripId]);


  /* ================= FORMAT TIME ================= */
  const formatTime = (time) => {
    if (!time) return "";
    const [h, m] = time.split(":");
    const hour = parseInt(h);
    const ampm = hour >= 12 ? "PM" : "AM";
    return `${hour % 12 || 12}:${m} ${ampm}`;
  };

  /* ================= FORMAT DATE ================= */
  const formatDate = (dateStr) => {
    if (!dateStr) return "No date";

    const date = new Date(dateStr);
    return date.toDateString();
  };

  /* ================= ADD EVENT ================= */
  const addEvent = () => {
    if (!form.title || !form.date) return;

    const newEvent = {
      title: form.title,
      eventDate: form.date,
      eventTime: form.time,
      type: form.type,
      description: form.description
    };

    fetch(`http://localhost:8081/api/itinerary/trip/${tripId}`, {
      method: "POST",
      headers,
      body: JSON.stringify(newEvent)
    })
      .then((res) => res.json())
      .then((saved) => {
        setEvents((prev) => [...prev, saved]);

        setForm({
          title: "",
          date: "",
          time: "",
          type: "activity",
          customType: "",
          description: ""
        });
      })
      .catch(console.error);
  };

  /* ================= EDIT ================= */
  const saveEdit = () => {
    fetch(`http://localhost:8081/api/itinerary/${editing.id}`, {
      method: "PUT",
      headers,
      body: JSON.stringify(editing)
    })
      .then((res) => res.json())
      .then((updated) => {
        setEvents((prev) =>
          prev.map((e) => (e.id === updated.id ? updated : e))
        );
        setEditing(null);
      })
      .catch(console.error);
  };

  /* ================= DRAG ================= */
  const onDragEnd = async (result) => {
  if (!result.destination) return;

  const moved = events.find(
    (e) => e.id?.toString() === result.draggableId
  );

  if (!moved) return;

  const updated = {
    title: moved.title,
    eventDate: result.destination.droppableId,
    eventTime: moved.eventTime,
    type: moved.type,
    description: moved.description
  };

  // optimistic UI update
  setEvents((prev) =>
    prev.map((e) =>
      e.id === moved.id ? { ...moved, eventDate: updated.eventDate } : e
    )
  );

  try {
    const res = await fetch(
      `http://localhost:8081/api/itinerary/${moved.id}`,
      {
        method: "PUT",
        headers,
        body: JSON.stringify(updated)
      }
    );

    if (!res.ok) {
      throw new Error(await res.text());
    }

    const saved = await res.json();

    setEvents((prev) =>
      prev.map((e) =>
        e.id === saved.id ? saved : e
      )
    );

  } catch (err) {
    console.error("Drag save failed:", err);
  }
};


  const deleteEvent = async (id) => {
  try {
    const res = await fetch(
      `http://localhost:8081/api/itinerary/${id}`,
      {
        method: "DELETE",
        headers
      }
    );

    console.log("DELETE status:", res.status);

    if (!res.ok) {
      const text = await res.text();
      console.error("DELETE failed:", text);
      return;
    }

    setEvents((prev) => prev.filter((e) => e.id !== id));
    setEditing(null);
  } catch (err) {
    console.error("DELETE error:", err);
  }
};



  /* ================= GROUP EVENTS ================= */
  const grouped = (events || []).reduce((acc, e) => {
    const key = e.eventDate || "Unknown";
    if (!acc[key]) acc[key] = [];
    acc[key].push(e);
    return acc;
  }, {});

  Object.keys(grouped).forEach((key) => {
    grouped[key].sort((a, b) =>
      (a.eventTime || "").localeCompare(b.eventTime || "")
    );
  });

  const sortedDates = Object.keys(grouped).sort();

  return (
    <div className="calendar-container">
      <h1>Trip Calendar</h1>
          {trip && (
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
      )}


      {/* FORM */}
      <div className="form">
        <input
          placeholder="Event title"
          value={form.title}
          onChange={(e) =>
            setForm({ ...form, title: e.target.value })
          }
        />

        <input
          type="date"
          value={form.date}
          onChange={(e) =>
            setForm({ ...form, date: e.target.value })
          }
        />

        <input
          type="time"
          value={form.time}
          onChange={(e) =>
            setForm({ ...form, time: e.target.value })
          }
        />
 <div className="choice">
        <select
         className="test"
        
          value={form.type}
          onChange={(e) =>
            setForm({ ...form, type: e.target.value })
          }
        >
          <option value="activity">Activity</option>
          <option value="food">Food</option>
          <option value="travel">Travel</option>
          <option value="hotel">Hotel</option>
          <option value="other">Other</option>
         </select>
          </div>
          
        <textarea
          placeholder="Notes"
          value={form.description}
          onChange={(e) =>
            setForm({ ...form, description: e.target.value })
          }
        />

        <button onClick={addEvent}>Add Event</button>
      </div>

      {/* CALENDAR */}
      <DragDropContext onDragEnd={onDragEnd}>
        <div className="calendar">
          {sortedDates.map((date) => (
            <Droppable droppableId={date} key={date}>
              {(provided) => (
                <div
                  className="day-column"
                  ref={provided.innerRef}
                  {...provided.droppableProps}
                >
                  <h3>{formatDate(date)}</h3>

                  {grouped[date].map((event, index) => (
                    <Draggable
                      key={event.id}
                      draggableId={event.id.toString()}
                      index={index}
                    >
                      {(provided) => (
                        <div
                          className={`event-card ${event.type}`}
                          ref={provided.innerRef}
                          {...provided.draggableProps}
                          {...provided.dragHandleProps}
                          onClick={() => setEditing(event)}
                        >
                          <strong>{event.title}</strong>

                          {event.eventTime && (
                            <div>
                              🕒 {formatTime(event.eventTime)}
                            </div>
                          )}

                          <small>{event.type}</small>
                        </div>
                      )}
                    </Draggable>
                  ))}

                  {provided.placeholder}
                </div>
              )}
            </Droppable>
          ))}
        </div>
      </DragDropContext>

      {/* MODAL */}
      {editing && (
        <>
          <div
            className="modal-overlay"
            onClick={() => setEditing(null)}
          />

          <div className="modal">
            <h3>Edit Event</h3>

            <input
              value={editing.title || ""}
              onChange={(e) =>
                setEditing({
                  ...editing,
                  title: e.target.value
                })
              }
            />

            <input
              type="time"
              value={editing.eventTime || ""}
              onChange={(e) =>
                setEditing({
                  ...editing,
                  eventTime: e.target.value
                })
              }
            />

            <textarea
              value={editing.description || ""}
              onChange={(e) =>
                setEditing({
                  ...editing,
                  description: e.target.value
                })
              }
            />

            <button className="save-btn" onClick={saveEdit}>
              Save
            </button>

            <button
              className="cancel-btn"
              onClick={() => setEditing(null)}
            >
              Cancel
            </button>
                <button
                style={{
                  marginTop: "10px",
                  width: "100%",
                  background: "darkred",
                  color: "white",
                  padding: "8px",
                  borderRadius: "8px",
                  border: "none",
                  cursor: "pointer"
                }}
                onClick={() => deleteEvent(editing.id)}
              >
                Delete Event
              </button>

          </div>
        </>
      )}
    </div>
  );
}
