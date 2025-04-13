import axios from 'axios';

const API_URL = 'http://localhost:8080/api/favourites'; 

export const AddToFavourites = async (productKitId) => {
    try {
        const token = localStorage.getItem("jwt");
        const userId = localStorage.getItem("userId");

        const response = await axios.post(
            `${API_URL}`,
            null, 
            {
                params: { userId, productKitId }, 
                headers: { Authorization: `Bearer ${token}` },
            }
        );

        return response.data; 
    } catch (error) {
        console.error("Error adding to favorites:", error);
        return null;
    }
};


export const RemoveFromFavourites = async (productKitId) => {
    try {
        const token = localStorage.getItem("jwt");
        const userId = localStorage.getItem("userId");

        await axios.delete(`${API_URL}`, {
            params: { userId, productKitId },
            headers: { Authorization: `Bearer ${token}` },
        });

        return true; 
    } catch (error) {
        console.error("Error removing from favorites:", error);
        return false;
    }
};


export const GetUserFavourites = async () => {
    try {
        const token = localStorage.getItem("jwt");
        const userId = localStorage.getItem("userId");

        const response = await axios.get(`${API_URL}/${userId}`, {
            headers: { Authorization: `Bearer ${token}` },
        });

        return response.data.map(fav =>fav.productKitId); 
    } catch (error) {
        console.error("Error fetching favorites:", error);
        return [];
    }
};