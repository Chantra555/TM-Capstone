import ReactDOM from "react-dom/client";
import { useState } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import Navbar from "./componets/Navbar.jsx";
import Dashboard from "./pages/Dashboard.jsx";
import CreateTrip from "./pages/CreateTrip.jsx";
import Trips from "./componets/Trips.jsx";
import Login from "./pages/Login.jsx";
import Budget from "./componets/Budget.jsx";
import TripLayout from "./componets/TripLayout.jsx";
import TripOverview from "./componets/TripOverview.jsx";
import Itinerary from "./componets/Itinerary.jsx";

import Party from "./componets/Party.jsx";
import Lodging from "./componets/Lodging.jsx";
import Transportation from "./componets/Transportation.jsx";
import "./style.css";

function App() {
  const [user, setUser] = useState(() => {
    try {
      return localStorage.getItem("user") || null;
    } catch {
      return null;
    }
  });

  const handleLogout = () => {
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    setUser(null);
  };

  return (
    <BrowserRouter>
      <Navbar user={user} onLogout={handleLogout} />

      <Routes>
        {/* MAIN PAGES */}
        <Route path="/" element={<Dashboard />} />
        <Route path="/create" element={<CreateTrip />} />
        <Route path="/trips" element={<Trips />} />
        <Route path="/login" element={<Login onLogin={setUser} />} />

        {/* TRIP LAYOUT (NESTED ROUTES) */}
        <Route path="/trips/:tripId" element={<TripLayout />}>
          <Route index element={<TripOverview />} />
          <Route path="itinerary" element={<Itinerary />} />
          <Route path="budget" element={<Budget />} />
          
          <Route path ="party" element={<Party />} />
          <Route path ="lodging" element={<Lodging />} />
          <Route path ="transportation" element={<Transportation/>} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;

ReactDOM.createRoot(document.getElementById("app")).render(<App />);