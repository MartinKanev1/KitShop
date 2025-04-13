import React, { useState } from 'react';
import '../Styles/BuyNowModal.css';
import useUserInfo from '../hooks/useUserInfo';


const BuyNowModal = ({ isOpen, onClose, onConfirm, product, size, quantity }) => {
    const { user, loading, error } = useUserInfo();
    const [address, setAddress] = useState('');
  
    const handleConfirm = () => {
      if (!address.trim()) {
        alert('Please enter your shipping address');
        return;
      }
  
      onConfirm({ address });
    };
  
    if (!isOpen) return null;
  
    return (
      <div className="modal-overlay">
        <div className="modal-content">
          <h2>🛍 Confirm Purchase</h2>
  
          {/* User Info Section */}
          <div className="modal-section">
            <h4>👤 Your Info</h4>
            {loading ? (
              <p>Loading user info...</p>
            ) : error ? (
              <p>Error loading user data.</p>
            ) : (
              <>
                
                <p><strong>Name:</strong> {user.firstName} {user.lastName} </p>
                <p><strong>Phone Number:</strong> {user.phoneNumber}</p>
              </>
            )}
          </div>
  
          {/* Shipping Address Input */}
          <div className="modal-section">
            <label>📦 Shipping Address:</label>
            <textarea
              placeholder="Enter your shipping address"
              value={address}
              onChange={(e) => setAddress(e.target.value)}
              required
            />
          </div>
  
          {/* Order Info */}
          <div className="modal-section">
            <h4>🛒 Order Summary</h4>
            <p><strong>Product:</strong> {product.name}</p>
            <p><strong>Size:</strong> {size} <strong>Quantity:</strong> {quantity}</p>
             <p><strong>Payment Method:</strong> Cash on Delivery</p>
          </div>
  
          {/* Buttons */}
          <div className="modal-buttons">
            <button onClick={handleConfirm}>✅ Confirm Purchase</button>
            <button onClick={onClose}>❌ Cancel</button>
          </div>
        </div>
      </div>
    );
  };
  
  export default BuyNowModal;
