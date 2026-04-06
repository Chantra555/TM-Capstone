import { useState } from "react";

export default function Login() {
  const [isSignup, setIsSignup] = useState(false); // toggle login/signup
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState(""); // for signup

  function handleSubmit(e) {
    e.preventDefault();
    if (isSignup) {
      console.log("Sign up:", { name, username, password });
    } else {
      console.log("Login:", { username, password });
    }
  }

  return (
    <div className="login-page">
      <form className="login-card" onSubmit={handleSubmit}>
        <h1 className="login-title">{isSignup ? "Create an account" : "Welcome back"}</h1>

        {/* Name input only for signup */}
        {isSignup && (
          <input
            type="text"
            placeholder="Name"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        )}

        <input
          type="username"
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

        <button type="submit">{isSignup ? "Sign Up" : "Sign In"}</button>

        <p className="login-footer">
          {isSignup ? "Already have an account?" : "Don't have an account?"}{" "}
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
