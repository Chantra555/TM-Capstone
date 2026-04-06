import ReactDOM from "react-dom/client";
import { useState } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./componets/Navbar.jsx";
import Dashboard from "./pages/Dashboard.jsx";
import CreateTrip from "./pages/CreateTrip.jsx";
import Login from "./pages/Login.jsx";
import "./style.css";


function App() {
  const[use,setUser] = useState(null);
  return (
    <BrowserRouter>
      <Navbar />
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
