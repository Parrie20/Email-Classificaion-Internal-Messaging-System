import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Login = ({ setSenderEmail }) => {
  const [email, setEmail] = useState('');
  const navigate = useNavigate();

  const handleLogin = async () => {
    if (!email.trim()) {
      alert('Please enter your email');
      return;
    }

    try {
      const res = await fetch(`http://192.168.1.8:8080/api/messages/check-device/${email}`);
      const exists = await res.json();

      if (exists) {
        setSenderEmail(email);
        navigate('/messages');
      } else {
        alert('Device not registered! Please register first.');
        navigate('/lan-registration');
      }
    } catch (error) {
      console.error('Error during login check:', error);
      alert('Something went wrong. Try again.');
    }
  };

  return (
    <div>
      <h2 className="text-xl font-bold mb-4">Login to Messaging</h2>
      <input
        type="email"
        placeholder="Enter your registered email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        className="border p-2 mr-2"
      />
      <button
        className="bg-blue-500 text-white px-4 py-2 rounded"
        onClick={handleLogin}
      >
        Login
      </button>
    </div>
  );
};

export default Login;
