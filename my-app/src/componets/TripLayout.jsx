import { useParams, Outlet, NavLink } from "react-router-dom";
import "./tripLayout.css";
// NAVBAR on side // 

export default function TripLayout() {
  const { tripId } = useParams();

  return (
    <div className="trip-layout">
      
      {/* SIDEBAR */}
      <div className="trip-sidebar">
        <h2 className="trip-sidebar-title">Trip</h2>

        <nav className="trip-nav">

          <NavLink to={`/trips/${tripId}`} end className="nav-item">
            Overview
          </NavLink>

          <NavLink to={`/trips/${tripId}/itinerary`} className="nav-item">
            Itinerary
          </NavLink>

          <NavLink to={`/trips/${tripId}/budget`} className="nav-item">
            Budget
          </NavLink>

          

           <NavLink to={`/trips/${tripId}/lodging`} className="nav-item">
            Lodging
          </NavLink>

           <NavLink to={`/trips/${tripId}/transportation`} className="nav-item">
            Transportation 
          </NavLink>

            <NavLink to={`/trips/${tripId}/party`} className="nav-item">
            Party 
          </NavLink>
         
          


        </nav>
      </div>

      {/* CONTENT */}
      <div className="trip-content">
        <Outlet />
      </div>
    </div>
  );
}
