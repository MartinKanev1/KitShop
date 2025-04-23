import React, { useEffect, useState } from "react";
import MyOrdersCard from "../components/MyOrdersCard";
import { getOrdersByUserId, cancelOrder } from "../Services/OrderService"; 


const MyOrdersList = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
  
    const userId = localStorage.getItem("userId");
  
    useEffect(() => {
      const fetchOrders = async () => {
        try {
          
          const data = await getOrdersByUserId(userId);
          setOrders(data);
        } catch (err) {
          setError("Failed to load orders");
          console.error(err);
        } finally {
          setLoading(false);
        }
      };
  
      if (userId) {
        fetchOrders();
      } else {
        setError("User not logged in.");
        setLoading(false);
      }
    }, [userId]);

    const handleCancelOrder = async (orderId) => {
      try {
        await cancelOrder(orderId);
        alert('Order canceled!');
        
      } catch (err) {
        console.error(err);
        alert('Failed to cancel order');
      }
    };
  
    if (loading) return <p style={{ padding: "2rem" }}>Loading orders...</p>;
    if (error) return <p style={{ padding: "2rem", color: "red" }}>{error}</p>;
  
    return (
      <div style={{ padding: "2rem" }}>
        <h2>My Orders</h2>
        {orders.length === 0 ? (
          <p>No orders found.</p>
        ) : (
          orders.map((order) => (
            <MyOrdersCard key={order.orderId} order={order} onCancel={handleCancelOrder} />
          ))
        )}
      </div>
    );
  };
  
  export default MyOrdersList;
