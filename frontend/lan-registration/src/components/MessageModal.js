import React, { useState } from 'react';

const MessageModal = ({ recipient, senderEmail, onClose }) => {
  const [message, setMessage] = useState('');

  const sendMessage = async () => {
    if (!senderEmail || !recipient?.email || !message.trim()) {
      alert('Sender, recipient, and message content are required.');
      return;
    }

    try {
      const response = await fetch('http://192.168.1.8:8080/api/messages/send', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          senderEmail: senderEmail,
          receiverEmail: recipient.email,
          content: message
        })
      });

      if (response.ok) {
        alert('Message sent!');
        onClose();
      } else {
        alert('Failed to send message');
      }
    } catch (err) {
      console.error('Error sending message:', err);
      alert('Something went wrong.');
    }
  };

  return (
    <div className="fixed top-0 left-0 w-full h-full bg-black bg-opacity-30 flex justify-center items-center">
      <div className="bg-white p-4 rounded shadow-lg w-full max-w-md">
        <h3 className="text-lg font-bold mb-2">Message {recipient.displayName}</h3>
        <textarea
          className="w-full p-2 border mb-2"
          rows="4"
          value={message}
          onChange={e => setMessage(e.target.value)}
          placeholder="Type your message..."
        />
        <div className="flex justify-end">
          <button className="mr-2 px-4 py-2 bg-gray-400 text-white rounded" onClick={onClose}>Cancel</button>
          <button className="px-4 py-2 bg-green-500 text-white rounded" onClick={sendMessage}>Send</button>
        </div>
      </div>
    </div>
  );
};

export default MessageModal;
