import React from "react";

const OrderItem = ({ item }) => {
  const borderColor = "#add8e6";

  return (
    <div
      style={{
        display: "flex",
        alignItems: "center",
        marginBottom: "1rem",
      }}
    >
      <img
        src={item.imageUrl}
        alt={item.name}
        style={{
          width: "80px",
          height: "80px",
          objectFit: "cover",
          border: `2px solid ${borderColor}`,
          borderRadius: "4px",
          marginRight: "1rem",
        }}
      />
      <div>
        <p><strong>{item.name}</strong></p>
        <p>Size: {item.size}</p>
        <p>Quantity: {item.quantity}</p>
        <p>Price: ${item.price.toFixed(2)}</p>
      </div>
    </div>
  );
};

export default OrderItem;

  