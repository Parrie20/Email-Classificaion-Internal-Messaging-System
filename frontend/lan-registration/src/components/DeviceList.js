import React, { useState } from 'react';
import MessageModal from './MessageModal';

const DeviceList = ({ devices, senderEmail }) => {
  const [selectedRecipient, setSelectedRecipient] = useState(null);

  const openMessageModal = (device) => {
    setSelectedRecipient(device);
  };

  return (
    <div>
      <h3 className="text-xl font-semibold mb-2">💻 Registered Devices</h3>
      <ul>
        {devices.length === 0 ? (
          <li>No devices found on LAN.</li>
        ) : (
          devices.map((device, index) => (
            <li key={index} className="flex justify-between mb-2">
              <span>{device.displayName} ({device.email})</span>
              <button
                onClick={() => openMessageModal(device)}
                className="bg-blue-500 text-white px-2 py-1 rounded"
              >
                Message
              </button>
            </li>
          ))
        )}
      </ul>

      {selectedRecipient && (
        <MessageModal
          recipient={selectedRecipient}
          senderEmail={senderEmail}
          onClose={() => setSelectedRecipient(null)}
        />
      )}
    </div>
  );
};

export default DeviceList;
