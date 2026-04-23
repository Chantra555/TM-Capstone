import "./landing.css";
import { useNavigate } from "react-router-dom";

export default function Dashboard() {
  const navigate = useNavigate();

  return (
    <div className="landing">

    

      {/* HERO */}
      <div className="hero">

        {/* LEFT SIDE */}
        <div className="hero-left">
          <p className="tagline"></p>

          <h1>
            Plan your next journey here
           
          </h1>

          <p className="description">
            Create buisness trips, family vacations, or weekend plans and save important details, by keeping everything
            organized in one beautiful place.
          </p>

         
        </div>

        {/* RIGHT SIDE (FROSTED CARD) */}
        <div className="hero-card">
          <h3>Everything for your perfect trip</h3>

          <div className="feature">
            <span>✈️</span>
            <div>
              <h4>Create Trips</h4>
              <p>Build and organize all your trips in one place.</p>
            </div>
          </div>

          <div className="feature">
            <span>🗂️</span>
            <div>
              <h4>Save Details</h4>
              <p>Flights, stays, notes — all saved securely.</p>
            </div>
          </div>

          <div className="feature">
            <span>🧭</span>
            <div>
              <h4>Stay Organized</h4>
              <p>Access your plans anytime, anywhere.</p>
            </div>
          </div>

          <div className="feature">
            <span>🏖️</span>
            <div>
              <h4>Enjoy the Trip</h4>
              <p>Focus on memories, not logistics.</p>
            </div>
          </div>

          <button
            className="card-btn"
            onClick={() => navigate("/create")}
          >
           🌴 Create Your Trip
            
          </button>
        </div>

      </div>
    </div>
  );
}
