import { useState } from "react";
import { useNavigate } from "react-router-dom";

// import TripInfo from "../componets/TripInfo"
// import Destinations from "../componets/Destinations";
// import Itenirary from "../componets/Itenirary"; 
import Budget from "../componets/Budget";
// import Notes from "../componets/Notes";

export default function CreateTrip() {
  const navigate = useNavigate();

  const [activeTab, setActiveTab] = useState("info");

  const [trip, setTrip] = useState({
    name: "",
    notes: "",
    destinations: [],
    activities: [],
    budget: ""
  });

  const isLoggedIn = localStorage.getItem("token");

  const handleSave = () => {
    if (!isLoggedIn) {
      navigate("/login");
      return;
    }

    console.log("Saving trip:", trip);
  };

  return (
    <div className="create-trip-page">

      {/* NOT LOGGED IN VIEW */}
      {!isLoggedIn && (
        <div className="trip-content">
          <h1>Create Trip</h1>
          <p className="login-warning">
            Please login to create and save a trip
          </p>

            <button
      className="theme-button"
      onClick={() => navigate("/login")}
    >
      Go to Login
    </button>
  </div>
      )}

      {/* LOGGED IN VIEW */}
      {isLoggedIn && (
        <>
          {/* Sidebar */}
          <div className="trip-sidebar">
            {/* <button onClick={() => setActiveTab("info")}>Trip Info</button>
            <button onClick={() => setActiveTab("destinations")}>Destinations</button>
            <button onClick={() => setActiveTab("itenirary")}>Itenirary</button> */}
            <button onClick={() => setActiveTab("budget")}>Budget</button>
            {/* <button onClick={() => setActiveTab("notes")}>Notes</button> */}

            <button onClick={handleSave}>
              Save Trip
            </button>
          </div>

          {/* Content */}
          <div className="trip-content">
            {/* {activeTab === "info" && (
              <TripInfo trip={trip} setTrip={setTrip} />
            )}

            {activeTab === "destinations" && (
              <Destinations trip={trip} setTrip={setTrip} />
            )}

            {activeTab === "itenirary" && (
              <Itenirary trip={trip} setTrip={setTrip} />
            )} */}

            {activeTab === "budget" && (
              <Budget trip={trip} setTrip={setTrip} />
            )}

            {/* {activeTab === "notes" && (
              <Notes trip={trip} setTrip={setTrip} />
            )} */}
          </div>
        </>
      )}

    </div>
  );
}
