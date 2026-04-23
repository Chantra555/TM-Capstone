import { useState } from "react";



export default function CreateTrip() {
  const [trip, setTrip] = useState({
    name: "",
    destination: "",
    startDate: "",
    endDate: ""
  });

  const handleChange = (e) => {
    setTrip({ ...trip, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log(trip);
  };

  return (
    <form onSubmit={handleSubmit}>
      <h1>Create Trip</h1>

      <input name="name" placeholder="Trip Name" onChange={handleChange} />
      <input name="destination" placeholder="Destination" onChange={handleChange} />
      <input type="date" name="startDate" onChange={handleChange} />
      <input type="date" name="endDate" onChange={handleChange} />

      <button type="submit">Create Trip</button>
    </form>
  );
}
