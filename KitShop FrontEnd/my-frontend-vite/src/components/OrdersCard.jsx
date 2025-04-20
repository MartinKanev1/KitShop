// import React from "react";
// import OrderDetailsCard from "../components/OrderDetailsCard";

// const handleApprove = () => {
//     const confirmed = window.confirm("Do you approve this order?");
//     if (confirmed) {
//       onApprove(order.orderId);
//     }
//   };

// const AllOrdersCard = ({ order, onApprove }) => {
//   return (
//     <div
//       style={{
//         border: "1px solid #ccc",
//         borderRadius: "8px",
//         padding: "1rem",
//         marginBottom: "1.5rem",
//         backgroundColor: "#f9f9f9",
//       }}
//     >
//       <div style={{ marginBottom: "1rem" }}>
//         <p><strong>Order Status:</strong> {order.status}</p>
//         <p><strong>Address:</strong> {order.address}</p>
//         <p><strong>To User:</strong> {order.userTo}</p>
//         <p><strong>Total Amount:</strong> ${order.totalAmount.toFixed(2)}</p>
//         <p><strong>Placed on:</strong> {new Date(order.createdAt).toLocaleDateString()}</p>
//       </div>

//       <div>
//         <h4>Order Items:</h4>
//         {order.items.map((item, index) => (
//           <OrderDetailsCard key={index} item={item} />
//         ))}
//       </div>

//       <button
//   onClick={() => handleApprove(order.orderId)}
//   style={{
//     marginTop: "1rem",
//     padding: "0.5rem 1rem",
//     backgroundColor: "#4CAF50",
//     color: "white",
//     border: "none",
//     borderRadius: "4px",
//     cursor: "pointer",
//     fontWeight: "bold",
//   }}
// >
//   Approve Order
// </button>

//     </div>
//   );
// };

// export default AllOrdersCard;

import React from "react";
import OrderDetailsCard from "../components/OrderDetailsCard";

const AllOrdersCard = ({ order, onApprove }) => {
  const handleApprove = () => {
    const confirmed = window.confirm("Do you approve this order?");
    if (confirmed) {
      onApprove(order.orderId);
    }
  };

  const statusColor = order.status === "Approved" ? "#28a745" : "#f0ad4e";

  return (
    <div
      style={{
        border: "1px solid #e0e0e0",
        borderRadius: "12px",
        padding: "1.5rem",
        marginBottom: "2rem",
        backgroundColor: "#ffffff",
        boxShadow: "0 4px 12px rgba(0, 0, 0, 0.06)",
        maxWidth: "600px",
        margin: "2rem auto",
      }}
    >
      <div style={{ marginBottom: "1.25rem" }}>
        <p>
          <strong>Order Status:</strong>{" "}
          <span
            style={{
              backgroundColor: statusColor,
              color: "#fff",
              padding: "2px 10px",
              borderRadius: "12px",
              fontSize: "0.9rem",
              fontWeight: "bold",
            }}
          >
            {order.status}
          </span>
        </p>
        <p><strong>Address:</strong> {order.address}</p>
        <p><strong>To User with ID:</strong> {order.userTo}</p>
        <p><strong>Total Amount:</strong> ${order.totalAmount.toFixed(2)}</p>
        <p><strong>Placed on:</strong> {new Date(order.createdAt).toLocaleDateString()}</p>
      </div>

      <div style={{ marginBottom: "1.5rem" }}>
        <h4 style={{ marginBottom: "0.75rem" }}>Order Items:</h4>
        {order.items.map((item, index) => (
          <OrderDetailsCard key={index} item={item} />
        ))}
      </div>

      {order.status === "PENDING" && (
        <div style={{ textAlign: "center" }}>
          <button
            onClick={handleApprove}
            style={{
              padding: "0.6rem 1.2rem",
              backgroundColor: "#28a745",
              color: "white",
              border: "none",
              borderRadius: "6px",
              cursor: "pointer",
              fontWeight: "bold",
            }}
          >
            Approve Order
          </button>
        </div>
      )}
    </div>
  );
};

export default AllOrdersCard;

