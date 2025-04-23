import axios from "axios";
import { jwtDecode } from "jwt-decode"; 


const API_URL = "http://localhost:8080/auth";

export const registerUser = async (userData) => {
    try {
        
        const response = await axios.post(`${API_URL}/register`, userData, {
            headers: {
                "Content-Type": "application/json",
            },
        });


        if (response.data.token) {
            localStorage.setItem("jwt", response.data.token); 
            const decodedToken = jwtDecode(response.data.token);
            const userEmail = decodedToken.sub;
            if (userEmail) {
                localStorage.setItem("userEmail", userEmail); 
                

                
                const userIdResponse = await axios.get(`http://localhost:8080/api/users/id-by-email/${userEmail}`, {
                    headers: { Authorization: `Bearer ${response.data.token}` },
                });

                if (userIdResponse.data.userId) {
                    localStorage.setItem("userId", userIdResponse.data.userId); 
                    
                } else {
                    console.error(" User ID not found for email:", userEmail);
                }
            } else {
                console.error(" Email not found in JWT token!");
            }
        } else {
            console.error(" No JWT token received from login API.");
        }

        

        return response.data;
    } catch (error) {
        console.error("Registration failed:", error);
        throw error;
    }
};



export const loginUser = async (email, password) => {
    try {
        const response = await axios.post(`${API_URL}/login`, { email, password }, {
            headers: { "Content-Type": "application/json" }
        });

        if (response.data.token) {
            localStorage.setItem("jwt", response.data.token); 
            const decodedToken = jwtDecode(response.data.token);
            const userEmail = decodedToken.sub;
            if (userEmail) {
                localStorage.setItem("userEmail", userEmail); 
                console.log(" Email extracted from JWT:", userEmail); 

                
                const userIdResponse = await axios.get(`http://localhost:8080/api/users/id-by-email/${userEmail}`, {
                    headers: { Authorization: `Bearer ${response.data.token}` },
                });

                if (userIdResponse.data.userId) {
                    localStorage.setItem("userId", userIdResponse.data.userId); 
                    console.log(" Stored userId:", userIdResponse.data.userId); 
                } else {
                    console.error(" User ID not found for email:", userEmail);
                }
            } else {
                console.error(" Email not found in JWT token!");
            }
        } else {
            console.error(" No JWT token received from login API.");
        }

        

        return response.data;
    } catch (error) {
        console.error("Login failed:", error);
        throw error;
    }
};