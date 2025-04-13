import axios from 'axios';

const API_URL = "http://localhost:8080/api/product-kits";

export const addProduct = async (formDataObject) => {
  const { imageFile, ...dtoData } = formDataObject;

  const token = localStorage.getItem("jwt"); 

  const formData = new FormData();
  formData.append('file', imageFile);

  
  const dtoBlob = new Blob([JSON.stringify(dtoData)], {
    type: 'application/json',
  });
  formData.append('productKitsDTO', dtoBlob);

  try {
    const response = await axios.post('http://localhost:8080/api/product-kits', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error('Error adding product:', error);
    throw error;
  }
};



export const getProductById = async (id) => {
  const token = localStorage.getItem('jwt');

  try {
    const response = await axios.get(`${API_URL}/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error(`Error fetching product with ID ${id}:`, error);
    throw error;
  }
};




export const updateProduct = async (id, formDataObject) => {
  const { imageFile, ...dtoData } = formDataObject;
  const token = localStorage.getItem('jwt');

  const formData = new FormData();

  
  if (imageFile) {
    formData.append('file', imageFile);
  }

  const dtoBlob = new Blob([JSON.stringify(dtoData)], {
    type: 'application/json',
  });
  formData.append('productKitsDTO', dtoBlob);

  try {
    const response = await axios.put(`${API_URL}/${id}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error(`Error updating product with ID ${id}:`, error);
    throw error;
  }
};

export const addReview = async (productId, reviewText) => {
  const token = localStorage.getItem('jwt');
  const userId = localStorage.getItem('userId'); 

  const payload = {
    comment: reviewText,
    userId: userId,
  };

  try {
    const response = await axios.post(
      `${API_URL}/${productId}/addReview`,
      payload,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error('❌ Error adding review:', error);
    throw error;
  }
};

export const getAllReviewsForProduct = async (productId) => {
  const token = localStorage.getItem("jwt");

  try {
    const response = await axios.get(`${API_URL}/${productId}/getReviews`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("❌ Error fetching reviews:", error);
    throw error;
  }
};

export const getAllProducts = async () => {
  const token = localStorage.getItem('jwt');

  try {
    const response = await axios.get(API_URL, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    return response.data;
  } catch (error) {
    console.error('Error fetching all products:', error);
    throw error;
  }
};


export const searchProductKits = async (keyword) => {
  const token = localStorage.getItem('jwt');

  try {
    const response = await axios.get(`${API_URL}/search`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      params: {
        keyword,
      },
    });

    return response.data;
  } catch (error) {
    console.error('Error searching for product kits:', error);
    throw error;
  }
};