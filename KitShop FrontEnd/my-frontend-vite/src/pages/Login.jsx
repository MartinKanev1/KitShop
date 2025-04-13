import { useState } from "react";
import "../Styles/Register.css";
import Header from "../components/Header";
import { Link, useNavigate } from "react-router-dom";
import Messi from "../assets/messi.png";
import Ronaldo from "../assets/ronaldo.png";
import Footer from "../components/Footer";
import { loginUser } from "../Services/AuthService"; 

function Login() {
  const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");



  const handleSubmit = async (e) => {
    e.preventDefault();
    

    try {
        const response = await loginUser(email, password);
        console.log("Login successful:", response);
        alert("Login successful!");
        navigate("/home"); 
      } catch (error) {
        console.error("Login error:", error);
        setError("Invalid email or password. Please try again.");
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
              <label>Email:</label>
              <input
                type="email"
                name="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)} 
                required
              />
            </div>

            <div>
              <label>Password:</label>
              <input
                type="password"
                name="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)} 
                required
              />
            </div>

            

            <button type="submit">Login</button>
          </form>

          <p className="login-link">
            Don't have an account? <Link to="/register">Go to register</Link>
          </p>
        </div>

        
        <div className="image-box show">{ <img src={Ronaldo}  /> }</div>
      </div>

      <Footer/>
    </div>

    
  
    
  );
}

export default Login;
