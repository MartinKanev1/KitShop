import { useState } from "react";
import "../Styles/Register.css";
import Header from "../components/Header";
import { Link,useNavigate } from "react-router-dom";
import Messi from "../assets/messi.png";
import Ronaldo from "../assets/ronaldo.png";
import Footer from "../components/Footer";
import { registerUser } from "../Services/AuthService"; 

function Register() {
  const navigate = useNavigate();
  const [error, setError] = useState("");
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    phoneNumber: "",
    firstName: "",
    lastName: ""
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await registerUser(formData);
      console.log("Registration successful:", response);
      alert("Registration successful!");
      navigate("/home"); 
    } catch (error) {
      console.error("Registration error:", error);
      setError("Registration failed. Please try again.");
    }
  };

  return (
    <div className="register-page">
      <Header showButtons={false} />

      <div className="register-content">
       
        <div className="image-box show">{ <img src={Messi}  /> }</div>

       
        <div className="register-form-container">
          <h2>Wellcome!</h2>
          <form className="register-form" onSubmit={handleSubmit}>

          <div>
              <label>First Name:</label>
              <input
                type="firstName"
                name="firstName"
                value={formData.firstName}
                onChange={handleChange}
                required
              />
            </div>

            <div>
              <label>Last Name:</label>
              <input
                type="lastName"
                name="lastName"
                value={formData.lastName}
                onChange={handleChange}
                required
              />
            </div>

            <div>
              <label>Email:</label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </div>

            <div>
              <label>Password:</label>
              <input
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                required
              />
            </div>

            <div>
              <label>Phone Number:</label>
              <input
                type="phoneNumber"
                name="phoneNumber"
                value={formData.phoneNumber}
                onChange={handleChange}
                required
              />
            </div>

            <button type="submit">Register</button>
          </form>

          <p className="login-link">
            Already have an account? <Link to="/login">Go to login</Link>
          </p>
        </div>

        
        <div className="image-box show">{ <img src={Ronaldo}  /> }</div>
      </div>

      <Footer/>
    </div>

    
  
    
  );
}

export default Register;
