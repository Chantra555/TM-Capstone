import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import "./party.css";

export default function Party() {
  const { tripId } = useParams(); // ✅ FIX: get ID from URL

  const [owner, setOwner] = useState(null);
  const [members, setMembers] = useState([]);
  const [usernameToAdd, setUsernameToAdd] = useState("");
  const [loading, setLoading] = useState(true);

  const token = localStorage.getItem("token");
  const currentUserId = localStorage.getItem("userId");

  const isOwner =
  owner &&
  currentUserId &&
  Number(owner.id) === Number(currentUserId);
  console.log("OWNER ID:", owner?.id);
console.log("CURRENT USER ID:", currentUserId);
console.log("IS OWNER:", isOwner);


  // =========================
  // FETCH MEMBERS
  // =========================
  const fetchMembers = async () => {
    try {
      const res = await fetch(
        `http://localhost:8081/api/trips/${tripId}/members`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!res.ok) throw new Error("Failed members fetch");

      const data = await res.json();

      setOwner(data.owner);
      setMembers(data.members || []);
      setLoading(false);
    } catch (err) {
      console.error("Failed loading party", err);
    }
  };

  useEffect(() => {
    if (!tripId) return;
    fetchMembers();
  }, [tripId]);

  // =========================
  // ADD USER BY USERNAME
  // =========================
  const handleAddUser = async () => {
    if (!usernameToAdd) return;

    try {
      await fetch(
        `http://localhost:8081/api/trips/${tripId}/members/by-username/${usernameToAdd}`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setUsernameToAdd("");
      fetchMembers();
    } catch (err) {
      console.error("Failed to add user", err);
    }
  };

  // =========================
  // REMOVE USER
  // =========================
  const handleRemoveUser = async (userId) => {
    try {
      await fetch(
        `http://localhost:8081/api/trips/${tripId}/members/${userId}`,
        {
          method: "DELETE",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      fetchMembers();
    } catch (err) {
      console.error("Failed to remove user", err);
    }
  };

  // =========================
  // LOADING STATE
  // =========================
  if (loading) return <div className="members-card">Loading party...</div>;

  return (
    <div className="members-card">
      <h2>👥 Party Members</h2>

      {/* OWNER */}
      <div className="owner-info">
        👑 Owner: {owner?.username}
      </div>

      {/* MEMBERS */}
      <div className="members-list">
        {members.length === 0 ? (
          <p>No members yet.</p>
        ) : (
          members.map((member) => (
            <div key={member.id} className="member-row">
              <span>👤 {member.username}</span>

              {isOwner && (
                <button onClick={() => handleRemoveUser(member.id)}>
                  ❌
                </button>
              )}
            </div>
          ))
        )}
      </div>

      {/* ADD MEMBER */}
      {isOwner && (
        <div className="add-member">
          <input
            type="text"
            placeholder="Enter username"
            value={usernameToAdd}
            onChange={(e) => setUsernameToAdd(e.target.value)}
          />

          <button onClick={handleAddUser}>➕ Add User</button>
        </div>
      )}
    </div>
  );
}
