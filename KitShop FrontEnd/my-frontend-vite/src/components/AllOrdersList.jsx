
import React, { useEffect, useState } from "react";
import OrdersCard from "../components/OrdersCard";
import { getAllOrders, ApproveOrders } from "../Services/OrderService";
import { CircularProgress, Alert } from "@mui/material";

const AllOrdersList = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [statusFilter, setStatusFilter] = useState("PENDING");

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

  if (loading)
    return (
      <div style={{ display: "flex", justifyContent: "center", marginTop: "4rem" }}>
        <CircularProgress />
      </div>
    );

  if (error)
    return (
      <div style={{ padding: "2rem" }}>
        <Alert severity="error">{error}</Alert>
      </div>
    );

  return (
    <div style={{ padding: "2rem", maxWidth: "800px", margin: "0 auto" }}>
      <h2 style={{ textAlign: "center", marginBottom: "1.5rem" }}>Orders</h2>

      
      <div
        style={{
          marginBottom: "1.5rem",
          display: "flex",
          flexWrap: "wrap",
          gap: "10px",
          justifyContent: "center",
        }}
      >
        {["PENDING", "Approved"].map((status) => (
          <button
            key={status}
            onClick={() => setStatusFilter(status)}
            style={{
              padding: "0.5rem 1rem",
              backgroundColor: statusFilter === status ? "#007bff" : "#f0f0f0",
              color: statusFilter === status ? "#fff" : "#333",
              border: "1px solid #ccc",
              borderRadius: "5px",
              cursor: "pointer",
              fontWeight: "bold",
              minWidth: "100px",
            }}
          >
            {status}
          </button>
        ))}
      </div>

      
      {filteredOrders.length === 0 ? (
        <p style={{ textAlign: "center" }}>
          No {statusFilter.toLowerCase()} orders found.
        </p>
      ) : (
        filteredOrders.map((order) => (
          <div key={order.orderId} style={{ marginBottom: "1.5rem" }}>
            <OrdersCard order={order} onApprove={handleApproveOrder} />
          </div>
        ))
      )}
    </div>
  );
};

export default AllOrdersList;

