import axios from 'axios';

const API_URL = 'http://localhost:8080/api/orders'; 

export const buyProduct = async (userId, productId, size, quantity, shippingAddress) => {
  const token = localStorage.getItem('jwt');

  try {
    const response = await axios.post(
      `${API_URL}/buy`,
      null, 
      {
        params: {
          userId,
          productId,
          size,
          quantity,
          shippingAddress,
        },
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    return response.data; 
  } catch (error) {
    console.error('🛑 Error purchasing product:', error);
    throw error;
  }
};


export const checkoutOrder = async (userId, shippingAddress) => {
  const token = localStorage.getItem('jwt');

  try {
    const response = await axios.post(
      `${API_URL}/checkout/${userId}`,
      null, 
      {
        params: {
          shippingAddress,
        },
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    return response.data; 
  } catch (error) {
    console.error('Checkout failed:', error);
    throw error;
  }
};

export const getOrdersByUserId = async (userId) => {
  const token = localStorage.getItem('jwt');

  try {
    const response = await axios.get(`${API_URL}/AllUserOrders/${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    return response.data;
  } catch (error) {
    console.error('Fetching orders failed:', error);
    throw error;
  }
};

export const cancelOrder = async (orderId) => {
  const token = localStorage.getItem('jwt');

  try {
    const response = await axios.post(
      `${API_URL}/${orderId}/cancel`,
      {},
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    return response.data; 
  } catch (error) {
    console.error(`🛑 Error cancelling order ${orderId}:`, error);
    throw error;
  }
};
