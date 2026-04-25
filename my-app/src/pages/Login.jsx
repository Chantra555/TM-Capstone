import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./login.css";

export default function Login({ onLogin }) {
  const [isSignup, setIsSignup] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");

  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault();

    const url = isSignup
      ? "http://localhost:8081/api/auth/signup"
      : "http://localhost:8081/api/auth/login";

    const body = isSignup
      ? { name, username, password }
      : { username, password };

    try {
      const response = await fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
      });

      if (!response.ok) {
        throw new Error(isSignup ? "Signup failed" : "Login failed");
      }

      const data = await response.json();

      // =========================
      // LOGIN FLOW
      // =========================
      if (!isSignup) {
        const token = data.token;

        if (!token) {
          throw new Error("No token received from server");
        }

        // ✅ FIX: store EVERYTHING Party needs
        localStorage.setItem("token", token);
        localStorage.setItem("userId", data.userId);       // 🔥 IMPORTANT FIX
        localStorage.setItem("username", data.username);   // 🔥 IMPORTANT FIX

        if (onLogin) onLogin(data.username);

        navigate("/trips"); // (better than "/")
        return;
      }

      // =========================
      // SIGNUP FLOW
      // =========================
      console.log("Signup success:", data);

      setIsSignup(false);
      alert("Account created! Please log in.");

    } catch (error) {
      console.error(error);
      alert(error.message);
    }
  }

  return (
    <div className="login-page">
      <form className="login-card" onSubmit={handleSubmit}>
        <h1 className="login-title">
          {isSignup ? "Create an account" : "Welcome back"}
        </h1>

        {isSignup && (
          <input
            type="text"
            placeholder="Name"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        )}

        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />

        <button type="submit">
          {isSignup ? "Sign Up" : "Sign In"}
        </button>

        <p className="login-footer">
          {isSignup ? "Already have an account?" : "Don't have an account?"}{" "}
          <span
            className="signup-link"
            style={{ color: "red", cursor: "pointer" }}
            onClick={() => setIsSignup(!isSignup)}
          >
            {isSignup ? "Login" : "Sign up"}
          </span>
        </p>
      </form>
    </div>
  );
}
