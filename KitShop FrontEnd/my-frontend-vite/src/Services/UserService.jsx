import axios from "axios";

const API_URL = "http://localhost:8080/api/users";

export const getUserInfo = async () => {
    try {
      const token = localStorage.getItem("jwt"); 
      const userId = localStorage.getItem("userId");
      const response = await axios.get(`${API_URL}/${userId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      return response.data;
    } catch (error) {
      console.error("Failed to fetch user info:", error);
      throw error;
    }
  };


  export const updateUserInfo = async (userId, updatedUser) => {
    try {
      const token = localStorage.getItem("jwt");
      const response = await axios.put(`${API_URL}/${userId}`, updatedUser, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      return response.data;
    } catch (error) {
      console.error("Failed to update user:", error);
      throw error;
    }
  };

  export const deleteUser = async (userId) => {
    try {
      const token = localStorage.getItem("jwt");
      await axios.delete(`${API_URL}/${userId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
    } catch (error) {
      console.error("Failed to delete user:", error);
      throw error;
    }
  };
  