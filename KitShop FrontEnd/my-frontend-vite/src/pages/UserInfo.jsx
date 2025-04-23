import React, { useState, useEffect } from "react";
import Header from '../components/Header';
import Footer from "../components/Footer";
import { useNavigate } from 'react-router-dom';
import { getUserInfo, updateUserInfo, deleteUser } from '../Services/UserService';
import { TextField, Typography, Paper, Stack, Button, CircularProgress} from '@mui/material';


const UserInfo = () => {
    const navigate = useNavigate();
    const [user, setUser] = useState(null);        
    const [tempUser, setTempUser] = useState(null); 
    const [isReadOnly, setIsReadOnly] = useState(true);
    const [loading, setLoading] = useState(true);
  
   

    useEffect(() => {
      const fetchUser = async () => {
        try {
          const userData = await getUserInfo();
          setUser(userData);
          setTempUser(userData); 
          setLoading(false);
        } catch (error) {
          console.error("Failed to load user data", error);
          setLoading(false);
        }
      };
  
      fetchUser();
    }, []);
  
    const handleChange = (e) => {
      setTempUser({ ...tempUser, [e.target.name]: e.target.value });
    };
  
    const handleEdit = () => setIsReadOnly(false);
  
    const handleCancel = () => {
      setTempUser(user);     
      setIsReadOnly(true);
    };
  
    const handleSave = async () => {
        try {
          const userId = localStorage.getItem("userId");
          const updatedUser = await updateUserInfo(userId, tempUser);
          setUser(updatedUser);        
          setTempUser(updatedUser);    
          setIsReadOnly(true);
          
        } catch (error) {
          console.error("Error saving changes:", error);
          
        }
      };


      const handleDelete = async () => {
        const userId = localStorage.getItem("userId");
        const confirm = window.confirm("Are you sure you want to delete your account? This action cannot be undone.");
    
        if (!confirm) return;
    
        try {
          await deleteUser(userId);
    
          
          localStorage.removeItem("jwt");
          localStorage.removeItem("userId");
    
          navigate("/register"); 
        } catch (error) {
          console.error("Error deleting user:", error);
          
        }
      };
  
    return (
      <>
        <Header showButtons={true} />
  
                  
       


{loading ? (
          <CircularProgress />
        ) : (
          <Paper elevation={3} sx={{ padding: 4, width: 400, bgcolor: '#cfd1d0', borderRadius: 2 }}>
            <Typography variant="h5" align="center" gutterBottom>
              User Details
            </Typography>

            <TextField
              fullWidth
              margin="normal"
              label="First Name"
              name="firstName"
              value={tempUser.firstName || ''}
              onChange={handleChange}
              InputProps={{ readOnly: isReadOnly }}
            />
            <TextField
              fullWidth
              margin="normal"
              label="Last Name"
              name="lastName"
              value={tempUser.lastName || ''}
              onChange={handleChange}
              InputProps={{ readOnly: isReadOnly }}
            />
            <TextField
              fullWidth
              margin="normal"
              label="Email"
              name="email"
              value={tempUser.email || ''}
              onChange={handleChange}
              InputProps={{ readOnly: isReadOnly }}
            />
            <TextField
              fullWidth
              margin="normal"
              label="Password"
              type="password"
              name="password"
              value={tempUser.password || ''}
              onChange={handleChange}
              InputProps={{ readOnly: isReadOnly }}
            />
            <TextField
              fullWidth
              margin="normal"
              label="Phone Number"
              name="phoneNumber"
              value={tempUser.phoneNumber || ''}
              onChange={handleChange}
              InputProps={{ readOnly: isReadOnly }}
            />

            <Stack direction="row" spacing={2} mt={3} justifyContent="center">
              {isReadOnly ? (
                <Button variant="contained" onClick={handleEdit}>
                  Edit
                </Button>
              ) : (
                <>
                  <Button variant="contained" color="success" onClick={handleSave}>
                    Save Changes
                  </Button>
                  <Button variant="outlined" color="error" onClick={handleCancel}>
                    Cancel
                  </Button>

                   <Button
                    variant="outlined"
                    color="error"
                    onClick={handleDelete}
                    sx={{ mt: 2 }}
                    >
                     Delete Account
                    </Button>

                </>
              )}
            </Stack>
          </Paper>
        )}
        
  
        <Footer />
      </>
    );
  };
  
  export default UserInfo;