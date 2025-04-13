import React, { useState } from 'react';
import '../Styles/BuyNowModal.css'; // reuse your existing modal styling
import useUserInfo from '../hooks/useUserInfo';

const CheckoutModal = ({ isOpen, onClose, onConfirm, cartItems }) => {
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
        <h2>🛍 Confirm Checkout</h2>

        {/* User Info */}
        <div className="modal-section">
          <h4>👤 Your Info</h4>
          {loading ? (
            <p>Loading user info...</p>
          ) : error ? (
            <p>Error loading user data.</p>
          ) : (
            <>
              <p><strong>Name:</strong> {user.firstName} {user.lastName}</p>
              <p><strong>Phone Number:</strong> {user.phoneNumber}</p>
            </>
          )}
        </div>

        {/* Shipping Address */}
        <div className="modal-section">
          <label>📦 Shipping Address:</label>
          <textarea
            placeholder="Enter your shipping address"
            value={address}
            onChange={(e) => setAddress(e.target.value)}
            required
          />
        </div>
 
        {/* Cart Items Summary */}
        <div className="modal-section">
          <h4>🛒 Order Summary</h4>
          {cartItems.map((item, index) => (
            <div key={item.cartItemId || index} style={{ marginBottom: '10px' }}>
              <p><strong>Product:</strong> {item.name}</p>
              <p><strong>Size:</strong> {item.size} <strong>Qty:</strong> {item.quantity}</p>
              <p><strong>Price:</strong> ${item.price} each</p>
              <hr />
            </div>
          ))}
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

export default CheckoutModal;
