import { Outlet } from "react-router-dom";
import Sidebar from "./Sidebar";

export default function Layout() {
  return (
    <div style={{ display: "flex", minHeight: "100vh" }}>
      {/* LEFT NAV */}
      <Sidebar />

      {/* MAIN CONTENT */}
      <div style={{ flex: 1, background: "#f6f7fb" }}>
        <Outlet />
      </div>
    </div>
  );
}
