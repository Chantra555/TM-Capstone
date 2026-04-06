import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Login({ onLogin }) {
  const [isSignup, setIsSignup] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");

  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault();

    try {
      const url = isSignup
        ? "http://localhost:8080/api/auth/signup"
        : "http://localhost:8080/api/auth/login";

      const body = isSignup
        ? { name, username, password }
        : { username, password };

      const response = await fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
      });

      if (!response.ok) {
        throw new Error("Request failed");
      }

      const data = await response.json();

      console.log("Success:", data);

      // store logged in user
      if (!isSignup && onLogin) {
        onLogin(data);
        navigate("/create");
      }

      // optional: after signup switch to login
      if (isSignup) {
        setIsSignup(false);
      }

    } catch (error) {
      console.error(error);
      alert(isSignup ? "Signup failed" : "Login failed");
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
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <button type="submit">
          {isSignup ? "Sign Up" : "Sign In"}
        </button>

        <p className="login-footer">
          {isSignup
            ? "Already have an account?"
            : "Don't have an account?"}{" "}
          <span
            className="signup-link"
            style={{ cursor: "pointer" }}
            onClick={() => setIsSignup(!isSignup)}
          >
            {isSignup ? "Login" : "Sign up"}
          </span>
        </p>
      </form>
    </div>
  );
}
