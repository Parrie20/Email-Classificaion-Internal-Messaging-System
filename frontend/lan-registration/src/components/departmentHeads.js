import React, { useState } from 'react';
import axios from 'axios';
import './departmentHeads.css';

const DepartmentHeadForm = () => {
  const [form, setForm] = useState({
    name: '',
    phoneNumber: '',
    email: '',
    department: ''
  });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post("http://localhost:8080/api/department-heads/register", form);
      alert(`Registered with ID: ${response.data.id}`);
    } catch (error) {
      alert("Error registering department head.");
      console.error(error);
    }
  };

  return (
    <div className="department-head-form">
      <h2>Register Department Head</h2>
      <form onSubmit={handleSubmit}>
        <input type="text" name="name" placeholder="Name" onChange={handleChange} required />
        <input type="text" name="phoneNumber" placeholder="Phone Number" onChange={handleChange} required />
        <input type="email" name="email" placeholder="Email" onChange={handleChange} required />
        <input type="text" name="department" placeholder="Department (e.g., SALES)" onChange={handleChange} required />
        <button type="submit">Register</button>
      </form>
    </div>
  );
};

export default DepartmentHeadForm;
