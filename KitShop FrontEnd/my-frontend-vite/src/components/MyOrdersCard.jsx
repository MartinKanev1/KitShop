import React from "react";
import OrderItem from '../components/OrderItem';


const MyOrdersCard = ({ order, onCancel }) => {
    const borderColor = "#add8e6"; // light blue

    const handleCancelClick = () => {
      const confirmed = window.confirm("Are you sure you want to cancel this order?");
      if (confirmed) {
        onCancel(order.orderId);
      }
    };
  
    return (
      <div
        style={{
          display: "flex",
          border: `2px solid ${borderColor}`,
          borderRadius: "8px",
          padding: "1rem",
          marginBottom: "2rem",
          maxWidth: "800px",
          backgroundColor: "white",
        }}
      >
        {/* Ordered Items List */}
        <div
          style={{
            flex: 3,
            padding: "1rem",
            border: `2px solid ${borderColor}`,
            borderRadius: "8px",
            marginRight: "1rem",
            backgroundColor: "white",
          }}
        >
          {order.items.map((item, index) => (
            <OrderItem key={index} item={item} />
          ))}
        </div>
  
        {/* Side Panel */}
        <div
          style={{
            flex: 1,
            borderLeft: `2px solid ${borderColor}`,
            padding: "1rem",
            display: "flex",
            flexDirection: "column",
            justifyContent: "space-between",
          }}
        >
          <div>
            <p><strong>Order Status:</strong> {order.status}</p>
            <p><strong>Address:</strong> {order.address}</p>
            <p><strong>Total Amount:</strong> ${order.totalAmount.toFixed(2)}</p>
            <p><strong>Placed on:</strong> {new Date(order.createdAt).toLocaleDateString()}</p>
          </div>
  
          <button
          onClick={handleCancelClick}
          disabled={order.status !== "PENDING"}
            style={{
              marginTop: "2rem",
              padding: "0.5rem 1rem",
              border: `2px solid ${borderColor}`,
              borderRadius: "6px",
              backgroundColor: "red",
              color: "white",
              cursor: "pointer",
              transition: "0.3s",
            }}
          >
            Cancel Order
          </button>
        </div>
      </div>
    );
  };
  
  export default MyOrdersCard;
