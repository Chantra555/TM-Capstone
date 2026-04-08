import ReactDOM from "react-dom/client";
import { useState } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./componets/Navbar.jsx";
import Dashboard from "./pages/Dashboard.jsx";
import CreateTrip from "./pages/CreateTrip.jsx";
import Login from "./pages/Login.jsx";
import "./style.css";


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
        <Route path="/login" element={<Login onLogin={setUser} />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;

ReactDOM.createRoot(document.getElementById("app")).render(<App />);