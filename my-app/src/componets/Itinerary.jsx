import React, { useState } from "react";
import {
  DragDropContext,
  Droppable,
  Draggable
} from "@hello-pangea/dnd";
import "./itinerary.css";

export default function ItineraryCalendar() {
  const [events, setEvents] = useState([
    {
      id: "1",
      title: "Flight to NYC",
      date: "2026-05-10",
      time: "08:00",
      type: "travel",
      description: "Arrive 2 hours early"
    }
  ]);

  const [form, setForm] = useState({
    title: "",
    date: "",
    time: "",
    type: "activity",
    customType: "",
    description: ""
  });

  const [editing, setEditing] = useState(null);

  /* ================= DATE FORMAT ================= */
  const formatDate = (dateStr) => {
    const date = new Date(dateStr);

    const dayName = date.toLocaleDateString("en-US", {
      weekday: "long"
    });

    const month = date.toLocaleDateString("en-US", {
      month: "long"
    });

    const day = date.getDate();

    const getSuffix = (d) => {
      if (d >= 11 && d <= 13) return "th";
      switch (d % 10) {
        case 1: return "st";
        case 2: return "nd";
        case 3: return "rd";
        default: return "th";
      }
    };

    return `${dayName}, ${month} ${day}${getSuffix(day)}`;
  };

  /* ================= ADD EVENT ================= */
  const addEvent = () => {
    if (!form.title.trim() || !form.date) return;

    const finalType =
      form.type === "other" && form.customType
        ? form.customType
        : form.type;

    const newEvent = {
      id: Date.now().toString(),
      title: form.title,
      date: form.date,
      time: form.time,
      type: finalType,
      description: form.description
    };

    setEvents([...events, newEvent]);

    setForm({
      title: "",
      date: "",
      time: "",
      type: "activity",
      customType: "",
      description: ""
    });
  };

  /* ================= EDIT ================= */
  const saveEdit = () => {
    setEvents(
      events.map((e) =>
        e.id === editing.id ? editing : e
      )
    );
    setEditing(null);
  };

  /* ================= DRAG ================= */
  const onDragEnd = (result) => {
    if (!result.destination) return;

    const { draggableId, destination } = result;

    setEvents(
      events.map((event) =>
        event.id === draggableId
          ? { ...event, date: destination.droppableId }
          : event
      )
    );
  };

  /* ================= GROUP ================= */
  const grouped = events.reduce((acc, event) => {
    if (!acc[event.date]) acc[event.date] = [];
    acc[event.date].push(event);
    return acc;
  }, {});

  Object.keys(grouped).forEach((date) => {
    grouped[date].sort((a, b) =>
      (a.time || "").localeCompare(b.time || "")
    );
  });

  const sortedDates = Object.keys(grouped).sort();

  // am/pm vs millitary 

  const formatTime = (time) => {
  if (!time) return "";

  const [hour, minute] = time.split(":");
  let h = parseInt(hour);
  const ampm = h >= 12 ? "PM" : "AM";

  h = h % 12 || 12; // convert 0 → 12

  return `${h}:${minute} ${ampm}`;
};


  return (
    <div className="calendar-container">
      <h1>Trip Calendar</h1>

      {/* ================= FORM ================= */}
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

          
        {form.type === "other" && (
          <input
            placeholder="Custom type..."
            value={form.customType}
            onChange={(e) =>
              setForm({
                ...form,
                customType: e.target.value
              })
            }
          />
        )}

        {/* 🆕 DESCRIPTION FIELD */}
        <textarea
          placeholder="Notes / description..."
          value={form.description}
          onChange={(e) =>
            setForm({
              ...form,
              description: e.target.value
            })
          }
        />

        <button onClick={addEvent}>Add Event</button>
      </div>

      {/* ================= EDIT MODAL ================= */}
      {editing && (
  <>
    <div className="modal-overlay" onClick={() => setEditing(null)} />

    <div className="modal">
      <h3>Edit Event</h3>

      <input
        value={editing.title}
        onChange={(e) =>
          setEditing({
            ...editing,
            title: e.target.value
          })
        }
      />

      <input
        type="time"
        value={editing.time}
        onChange={(e) =>
          setEditing({
            ...editing,
            time: e.target.value
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

      <button className="save-btn" onClick={saveEdit}>Save</button>
      <button className="cancel-btn" onClick={() => setEditing(null)}>Cancel</button>
    </div>
  </>
)}


      {/* ================= CALENDAR ================= */}
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
                      draggableId={event.id}
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

                          {event.time && (
                            <div>🕒 {formatTime(event.time)}</div>
                          )}

                          {/* 🆕 SHOW DESCRIPTION */}
                          {event.description && (
                            <p className="desc">
                              {event.description}
                            </p>
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
    </div>
  );
}
