import React, { useState, useEffect } from "react";
import Header from '../components/Header';
import Footer from "../components/Footer";
import { useNavigate } from 'react-router-dom';
import { getUserInfo, updateUserInfo, deleteUser } from '../Services/UserService';
import { TextField, Typography, Paper, Stack, Button, CircularProgress} from '@mui/material';


const UserInfo = () => {
    const navigate = useNavigate();
    const [user, setUser] = useState(null);        // will hold the actual user info
    const [tempUser, setTempUser] = useState(null); // used for editing
    const [isReadOnly, setIsReadOnly] = useState(true);
    const [loading, setLoading] = useState(true);
  
   

    useEffect(() => {
      const fetchUser = async () => {
        try {
          const userData = await getUserInfo();
          setUser(userData);
          setTempUser(userData); // initialize tempUser with user data
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
      setTempUser(user);     // Reset to original user data
      setIsReadOnly(true);
    };
  
    const handleSave = async () => {
        try {
          const userId = localStorage.getItem("userId");
          const updatedUser = await updateUserInfo(userId, tempUser);
          setUser(updatedUser);        // update local state with new data
          setTempUser(updatedUser);    // reset temp user to saved state
          setIsReadOnly(true);
          // You could also show a toast/snackbar here to confirm success
        } catch (error) {
          console.error("Error saving changes:", error);
          // Optionally show an error message to the user
        }
      };


      const handleDelete = async () => {
        const userId = localStorage.getItem("userId");
        const confirm = window.confirm("Are you sure you want to delete your account? This action cannot be undone.");
    
        if (!confirm) return;
    
        try {
          await deleteUser(userId);
    
          // Optionally clear localStorage and redirect
          localStorage.removeItem("jwt");
          localStorage.removeItem("userId");
    
          navigate("/register"); // or to login/landing page
        } catch (error) {
          console.error("Error deleting user:", error);
          // Show an error message if needed
        }
      };
  
    return (
      <>
        <Header showButtons={true} />
  
                  
       


{loading ? (
          <CircularProgress />
        ) : (
          <Paper elevation={3} sx={{ padding: 4, width: 400, bgcolor: 'lightblue', borderRadius: 2 }}>
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