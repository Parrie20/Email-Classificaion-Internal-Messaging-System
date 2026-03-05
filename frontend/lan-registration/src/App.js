import React, { useState } from 'react';
import { Routes, Route, Link, Navigate } from 'react-router-dom';
import LanRegistrationForm from './components/lan_registration';
import DepartmentHeadForm from './components/departmentHeads';
import MsgPage from './components/MessagePage';
import Login from './components/Login';

function App() {
  const [senderEmail, setSenderEmail] = useState(null);

  return (
    <div>
      <nav style={styles.nav}>
        <Link to="/" style={styles.link}>🏠 Home</Link>
        <Link to="/lan-registration" style={styles.link}>🌐 LAN Registration</Link>
        <Link to="/department-heads" style={styles.link}>👥 Department Heads</Link>
        <Link to="/messages" style={styles.link}>💬 Messaging</Link>
      </nav>

      <div style={styles.container}>
        <Routes>
          <Route path="/" element={<h2>Welcome to the Email Categorizer App!</h2>} />
          <Route path="/lan-registration" element={<LanRegistrationForm />} />
          <Route path="/department-heads" element={<DepartmentHeadForm />} />
          <Route path="/login" element={<Login setSenderEmail={setSenderEmail} />} />
          <Route
            path="/messages"
            element={
              senderEmail ? (
                <MsgPage senderEmail={senderEmail} />
              ) : (
                <Navigate to="/login" replace />
              )
            }
          />
        </Routes>
      </div>
    </div>
  );
}

const styles = {
  nav: {
    background: '#333',
    padding: '1rem',
    display: 'flex',
    justifyContent: 'space-around',
    marginBottom: '1rem'
  },
  link: {
    color: 'white',
    textDecoration: 'none',
    fontWeight: 'bold'
  },
  container: {
    padding: '1rem'
  }
};

export default App;
