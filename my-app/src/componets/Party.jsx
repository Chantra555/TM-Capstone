import { useState } from "react";
import "./party.css";

export default function Party() {
  const [party, setParty] = useState([
    
  ]);

  const [name, setName] = useState("");

  // ADD PERSON
  const addPerson = () => {
    if (!name.trim()) return;

    const newPerson = {
      id: Date.now(),
      name: name.trim(),
    };

    setParty(prev => [...prev, newPerson]);
    setName("");
  };

  // REMOVE PERSON
  const removePerson = (id) => {
    setParty(prev => prev.filter(p => p.id !== id));
  };

  return (
    <div className="party-page">
      <h2 className="title">Trip Party 🎉</h2>

      {/* ADD SECTION */}
      <div className="add-row">
        <input
          type="text"
          placeholder="Enter name..."
          value={name}
          onChange={(e) => setName(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && addPerson()}
        />
        <button onClick={addPerson}>Add</button>
      </div>

      {/* LIST */}
      <div className="party-list">
        {party.length === 0 ? (
          <p className="empty">No one added yet</p>
        ) : (
          party.map(person => (
            <div key={person.id} className="party-card">
              <div className="left">
                <div className="avatar">
                  {person.name.charAt(0).toUpperCase()}
                </div>
                <span>{person.name}</span>
              </div>

              <button
                className="remove-btn"
                onClick={() => removePerson(person.id)}
              >
                Remove
              </button>
            </div>
          ))
        )}
      </div>
    </div>
  );
}
