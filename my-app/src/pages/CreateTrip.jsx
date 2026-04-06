import { useState } from "react";

export default function CreateTrip() {
  const [trip, setTrip] = useState({
    name: "",
    destination: "",
    startDate: "",
    endDate: ""
  });

  const handleChange = (e) => {
    setTrip({ ...trip, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log(trip);
  };

  return (
    <form onSubmit={handleSubmit}>
      <h1>Create Trip</h1>

      <input name="name" placeholder="Trip Name" onChange={handleChange} />
      <input name="destination" placeholder="Destination" onChange={handleChange} />
      <input type="date" name="startDate" onChange={handleChange} />
      <input type="date" name="endDate" onChange={handleChange} />

      <button type="submit">Create Trip</button>
    </form>
  );
}

// import { useState } from "react";
// import AddActivityModal from "./AddActivityModal";
// import { TYPES, INITIAL_DAYS } from "./data";
// import "./CreateTrip.css"; // Import the CSS

// export default function CreateTrip({ isLoggedIn }) {
//   const [activeSection, setActiveSection] = useState("sight");
//   const [showModal, setShowModal] = useState(false);
//   const [items, setItems] = useState(INITIAL_DAYS[0].items);

//   function handleAddItem(data) {
//     setItems([...items, { id: Date.now(), ...data }]);
//     setShowModal(false);
//   }

//   const filteredItems = items.filter(item => item.type === activeSection);

//   if (!isLoggedIn) {
//     return (
//       <div style={{ padding: 40 }}>
//         <h2>Please log in to create a trip</h2>
//       </div>
//     );
//   }

//   return (
//     <div className="create-trip-container">
      
//       {/* Section tabs */}
//       <div className="section-tabs">
//         {TYPES.map(t => (
//           <button
//             key={t.key}
//             onClick={() => setActiveSection(t.key)}
//             className={`section-tab ${activeSection === t.key ? "active" : ""}`}
//           >
//             {t.label}
//           </button>
//         ))}
//       </div>

//       {/* Section items */}
//       <div>
//         {filteredItems.map(item => (
//           <div key={item.id} className="section-item">
//             <strong>{item.title}</strong> — {item.time}
//           </div>
//         ))}
//       </div>

//       {/* Add button */}
//       <button
//         onClick={() => setShowModal(true)}
//         className="add-button"
//       >
//         + Add {TYPES.find(t => t.key === activeSection).label}
//       </button>

//       {/* Modal */}
//       {showModal && (
//         <AddActivityModal
//           onClose={() => setShowModal(false)}
//           onAdd={(data) => handleAddItem({ ...data, type: activeSection })}
//         />
//       )}
//     </div>
//   );
// }
// // 
