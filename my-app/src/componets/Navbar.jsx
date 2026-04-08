import { Link, useNavigate } from "react-router-dom";

export default function Navbar() {
  const navigate = useNavigate();

  const isLoggedIn = localStorage.getItem("token");

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <nav className="navbar">
      <h2>Muro</h2>

      <div>
        <Link to="/">Dashboard</Link>
        <Link to="/create">Create Trip</Link>

        {isLoggedIn && <Link to="/trips">Your Trips</Link>}

        {!isLoggedIn ? (
          <Link to="/login">Login</Link>
        ) : (
          <a onClick={handleLogout} style={{ cursor: "pointer" }}>
            Logout
          </a>
        )}
      </div>
    </nav>
  );
}
