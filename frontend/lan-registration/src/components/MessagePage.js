import React, { useEffect, useState } from 'react';
import DeviceList from './DeviceList';
import MessageInbox from './MessageInbox';

const MessagesPage = ({ senderEmail }) => {
  const [devices, setDevices] = useState([]);
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    // Fetch LAN devices
    fetch('http://192.168.1.8:8080/devices')
      .then(res => res.json())
      .then(data => {
        setDevices(data || []);
      })
      .catch(err => {
        console.error('Failed to fetch devices:', err);
        setDevices([]);
      });

    // Fetch inbox messages using senderEmail
    if (senderEmail) {
      fetch(`http://192.168.1.8:8080/api/messages/inbox/${senderEmail}`)
        .then(res => res.json())
        .then(data => {
          setMessages(Array.isArray(data) ? data : []);
        })
        .catch(err => {
          console.error('Failed to fetch messages:', err);
          setMessages([]);
        });
    }
  }, [senderEmail]);

  return (
    <div className="p-4">
      <h2 className="text-2xl font-bold mb-4">📡 LAN Messaging System</h2>
      <div className="grid grid-cols-2 gap-4">
        <DeviceList devices={devices} senderEmail={senderEmail} />
        <MessageInbox messages={messages} />
      </div>
    </div>
  );
};

export default MessagesPage;
