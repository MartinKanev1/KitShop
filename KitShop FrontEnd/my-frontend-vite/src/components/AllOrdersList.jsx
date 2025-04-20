// import React, { useEffect, useState } from "react";
// import OrdersCard from "../components/OrdersCard";
// import { getAllOrders, ApproveOrders } from "../Services/OrderService"; // ✅ correct


// const AllOrdersList = () => {
//     const [orders, setOrders] = useState([]);
//     const [loading, setLoading] = useState(true);
//     const [error, setError] = useState("");
  
    
  
//     useEffect(() => {
//       const fetchOrders = async () => {
//         try {
          
//             const data = await getAllOrders();
//             setOrders(data);
//           } catch (err) {
//             setError("Failed to load orders");
//             console.error(err);
//           } finally {
//             setLoading(false);
//           }
//         };
    
//         fetchOrders();
//       }, []);

//       const handleApproveOrder = async (orderId) => {
//         try {
//           await ApproveOrders(orderId);
//           alert('Order canceled!');
          
//         } catch (err) {
//           console.error(err);
//           alert('Failed to cancel order');
//         }
//       };

    
  
//     if (loading) return <p style={{ padding: "2rem" }}>Loading orders...</p>;
//     if (error) return <p style={{ padding: "2rem", color: "red" }}>{error}</p>;
  
//     return (
//       <div style={{ padding: "2rem" }}>
//         <h2>Orders</h2>
//         {orders.length === 0 ? (
//           <p>No orders found.</p>
//         ) : (
//           orders.map((order) => (
//             <OrdersCard key={order.orderId} order={order} onApprove={handleApproveOrder}  />
//           ))
//         )}
//       </div>
//     );
//   };
  
//   export default AllOrdersList;

import React, { useEffect, useState } from "react";
import OrdersCard from "../components/OrdersCard";
import { getAllOrders, ApproveOrders } from "../Services/OrderService";

const AllOrdersList = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [statusFilter, setStatusFilter] = useState("PENDING"); // 👈 default to PENDING

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const data = await getAllOrders();
        setOrders(data);
      } catch (err) {
        setError("Failed to load orders");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, []);

  const handleApproveOrder = async (orderId) => {
    try {
      await ApproveOrders(orderId);
      // Update status in UI without refetching
      setOrders((prev) =>
        prev.map((order) =>
          order.orderId === orderId ? { ...order, status: "Approved" } : order
        )
      );
    } catch (err) {
      console.error(err);
      alert("Failed to approve order");
    }
  };

  const filteredOrders = orders.filter(
    (order) => order.status === statusFilter
  );

  if (loading) return <p style={{ padding: "2rem" }}>Loading orders...</p>;
  if (error) return <p style={{ padding: "2rem", color: "red" }}>{error}</p>;

  return (
    <div style={{ padding: "2rem" }}>
      <h2>Orders</h2>

      {/* 🔘 Two Filter Buttons */}
      <div style={{ marginBottom: "1.5rem" }}>
        {["PENDING", "Approved"].map((status) => (
          <button
            key={status}
            onClick={() => setStatusFilter(status)}
            style={{
              marginRight: "1rem",
              padding: "0.5rem 1rem",
              backgroundColor: statusFilter === status ? "#007bff" : "#f0f0f0",
              color: statusFilter === status ? "#fff" : "#333",
              border: "1px solid #ccc",
              borderRadius: "5px",
              cursor: "pointer",
              fontWeight: "bold",
            }}
          >
            {status}
          </button>
        ))}
      </div>

      {filteredOrders.length === 0 ? (
        <p>No {statusFilter.toLowerCase()} orders found.</p>
      ) : (
        filteredOrders.map((order) => (
          <OrdersCard
            key={order.orderId}
            order={order}
            onApprove={handleApproveOrder}
          />
        ))
      )}
    </div>
  );
};

export default AllOrdersList;


