
import React from "react";

const OrderDetailsCard = ({ item }) => {
  return (
    <div
      style={{
        display: "flex",
        alignItems: "center",
        gap: "1rem",
        padding: "1rem",
        border: "1px solid #ddd",
        borderRadius: "10px",
        backgroundColor: "#f9f9f9",
        marginBottom: "1rem",
      }}
    >
      
      {item.imageUrl && (
        <img
          src={item.imageUrl}
          alt={item.name}
          style={{
            width: "80px",
            height: "80px",
            objectFit: "cover",
            borderRadius: "8px",
          }}
        />
      )}

     
      <div>
        <p><strong>{item.name || `ProductID: ${item.id}`}</strong></p>
        <p>Size: {item.size}</p>
        <p>Quantity: {item.quantity}</p>
        <p>Price: ${item.price.toFixed(2)}</p>
      </div>
    </div>
  );
};

export default OrderDetailsCard;
