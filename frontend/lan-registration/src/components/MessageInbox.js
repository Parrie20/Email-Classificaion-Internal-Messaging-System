import React from 'react';

const MessageInbox = ({ messages, currentEmail }) => {
  return (
    <div>
      <h3 className="text-xl font-semibold mb-2">📨 Inbox</h3>
      <ul>
        {messages.length === 0 ? (
          <li>No messages received yet.</li>
        ) : (
          messages.map((msg, index) => (
            <li key={index} className="mb-2">
              <strong>{msg.senderEmail === currentEmail ? 'You' : msg.senderEmail}:</strong> {msg.content}
            </li>
          ))
        )}
      </ul>
    </div>
  );
};



export default MessageInbox;
