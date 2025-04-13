import axios from 'axios';

const API_URL = 'http://localhost:8080/api/shopping-cart'; 

export const addProductToCart = async (userId, productId, size, quantity) => {
  const token = localStorage.getItem('jwt');

  try {
    const response = await axios.post(
      `${API_URL}/add`,
      null, 
      {
        params: {
          userId,
          productId,
          size,
          quantity,
        },
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    return response.data; 
  } catch (error) {
    console.error('Error adding product to cart:', error);
    throw error;
  }
};

export const getCartItems = async (userId) => {
  const token = localStorage.getItem('jwt');

  try {
    const response = await axios.get(`${API_URL}/${userId}/items`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    return response.data;
  } catch (error) {
    console.error('Error fetching cart items:', error);
    throw error;
  }
};

export const getProductIdByCartItem = async (cartItemId) => {
  const token = localStorage.getItem('jwt');

  try {
    const response = await axios.get(
      `http://localhost:8080/api/shopping-cart/item/${cartItemId}/product`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    return response.data; 
  } catch (error) {
    console.error(`Error fetching productId for cartItemId ${cartItemId}:`, error);
    throw error;
  }
};



export const removeProductFromCart = async (userId, cartItemId) => {
  const token = localStorage.getItem('jwt');

  try {
    const response = await axios.delete(
      `http://localhost:8080/api/shopping-cart/${userId}/item/${cartItemId}`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    return response.data; 
  } catch (error) {
    console.error(`Error removing cart item ${cartItemId}:`, error);
    throw error;
  }
};


