import ReactDOM from "react-dom/client";
import { useState } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./componets/Navbar.jsx";
import Dashboard from "./pages/Dashboard.jsx";
import CreateTrip from "./pages/CreateTrip.jsx";
import Trips from "./componets/Trips.jsx";
import Login from "./pages/Login.jsx";
import "./style.css";
import Budget from "./componets/Budget.jsx"
import TripDetail from "./componets/TripDetail.jsx";
import TripLayout from "./componets/TripLayout.jsx";
import TripOverview from "./componets/TripOverview.jsx";
import Itinerary from "./componets/Itinerary.jsx";

function App() {
  // use null as initial state safely
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
        <Route path="/" element={<Dashboard />} />
        <Route path="/create" element={<CreateTrip />} />
          <Route path="/trips" element={<Trips />} />
        <Route path="/login" element={<Login onLogin={setUser} />} />
        <Route path="/trips/:id" element={<TripDetail />} />

        <Route path="/trips/:id" element={<TripLayout />}>
        <Route index element={<TripOverview />} />
        <Route path="Itinerary" element={<Itinerary/>} />
        <Route path="budget" element={<Budget />} />
        {/* <Route path="settings" element={<div>Settings</div>} /> */}
      </Route>

      </Routes>
    </BrowserRouter>
  );
}

export default App;

ReactDOM.createRoot(document.getElementById("app")).render(<App />);