import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ShoppingCart, Menu } from 'lucide-react';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import HomeIcon from '@mui/icons-material/Home';
import PersonIcon from '@mui/icons-material/Person';
import ShoppingBagIcon from '@mui/icons-material/ShoppingBag';
import AddBoxIcon from '@mui/icons-material/AddBox';
import ListAltIcon from '@mui/icons-material/ListAlt';
import LogoutIcon from '@mui/icons-material/Logout';
import '../Styles/Header.css';

const Header = ({ showButtons = true}) => {
  const navigate = useNavigate();
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const toggleSidebar = () => {
    setIsSidebarOpen((prev) => !prev);
  };

  const handleLogout = () => {
    
    navigate('/login');
  };

  return (
   
    
      <>
      
      {/* <header className="header">
      {showButtons && (
      <button className="menu-button" onClick={toggleSidebar}>
       <div className="menu-icon">
       <Menu size={22} />
       </div>
       <span className="menu-text"></span>
  
        </button>
        )}
        

         <h1 className="title">Football kits store</h1>
         
       

{showButtons && (
  <>
    <Link to="/my-favourite-products" className="header__button">
      <FavoriteBorderIcon style={{ fontSize: 20 }} />
    </Link>
    <Link to="/my-shopping-cart" className="header__button">
      <ShoppingCart size={18} />
      <span className="button-text">Cart</span>
    </Link>
  </>
)}

       </header> */}

<header className="header">
  {showButtons && (
    <div className="header-left">
      <button className="menu-button" onClick={toggleSidebar}>
        <div className="menu-icon">
          <Menu size={22} />
        </div>
      </button>
    </div>
  )}

  <div className="header-center">
    <h1 className="title">Football kits store</h1>
  </div>

  {showButtons && (
    <div className="header-right">
      <Link to="/my-favourite-products" className="header__button">
        <FavoriteBorderIcon style={{ fontSize: 24 }} />
      </Link>
      <Link to="/my-shopping-cart" className="header__button">
        <ShoppingCart size={20} />
        <span className="button-text">Cart</span>
      </Link>
    </div>
  )}
</header>


      

<nav className={`sidebar ${isSidebarOpen ? 'open' : ''}`}>
  <div className="sidebar-main">
    <div className="sidebar-header">
      <span>Home Menu</span>
      <button className="close-button" onClick={toggleSidebar}>✕</button>
    </div>

    <ul className="menu-list">
      <li>
        <Link to="/home">
          <HomeIcon style={{ marginRight: '8px' }} />
          Home
        </Link>
      </li>
      <li>
        <Link to="/user-info">
          <PersonIcon style={{ marginRight: '8px' }} />
          My Profile
        </Link>
      </li>
      <li>
        <Link to="/my-orders">
          <ShoppingBagIcon style={{ marginRight: '8px' }} />
          My Orders
        </Link>
      </li>
      <li>
        <Link to="/add-product">
          <AddBoxIcon style={{ marginRight: '8px' }} />
          Add Product
        </Link>
      </li>
      <li>
        <Link to="/orders">
          <ListAltIcon style={{ marginRight: '8px' }} />
          View Orders
        </Link>
      </li>
    </ul>
  </div>

  <div className="logout-button-container">
    <button onClick={handleLogout}>
      <LogoutIcon style={{ marginRight: '6px' }} />
      Logout
    </button>
  </div>
</nav>

 
     
    
  
  </>
)};







export default Header;
