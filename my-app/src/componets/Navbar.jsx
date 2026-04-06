import { Link } from "react-router-dom";


export default function Navbar() {
  return (
    <nav className="navbar">
      <h2>Muro</h2>
      <div>
        <Link to="/">Dashboard</Link>
        <Link to="/create">Create Trip</Link>
        <Link to="/login">Login</Link>
      </div>
    </nav>
  );
}
