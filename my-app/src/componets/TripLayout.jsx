// Nav Bar on left side when inside trip 



import { useParams, Outlet, NavLink } from "react-router-dom";
import "./tripLayout.css";

export default function TripLayout() {
  const { id } = useParams();

  return (
    <div className="trip-layout">
      
      {/* SIDEBAR */}
      <div className="trip-sidebar">
        <h2 className="trip-sidebar-title">Trip</h2>

        <nav className="trip-nav">
          <NavLink to={`/trips/${id}`} end className="nav-item">
            Overview - add Trip Name at Layout.jsx
          </NavLink>

          <NavLink to={`/trips/${id}/Itinerary`} className="nav-item">
            Itinerary -jsx no logic
          </NavLink>

          <NavLink to={`/trips/${id}/settings`} className="nav-item">
            Settings -nyc
          </NavLink>

          <NavLink to={`/trips/${id}/Budget`} className="nav-item">
            Budget
          </NavLink>

          <NavLink to={`/trips/${id}/settings`} className="nav-item">
            Documents -nyc
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
