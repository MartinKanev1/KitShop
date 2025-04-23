import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Register from './pages/Register';
import Login from './pages/Login';
import Home from "./pages/Home"
import UserInfo from "./pages/UserInfo"
import AddProduct from "./pages/AddProduct";
import EditProduct from "./pages/EditProduct";
import ViewProduct from './pages/ViewProduct';
import ShoppingCart from "./pages/ShoppingCart";
import Favorites from "./pages/Favourites";
import MyOrders from "./pages/MyOrders";
import ALLOrders from "./pages/ViewAllOrders";
import './App.css'

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/home" element={<Home/>} />
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/user-info" element={<UserInfo />} />
        <Route path="/add-product" element={<AddProduct />} />
        <Route path="/edit-product/:id" element={<EditProduct />} />
        <Route path="/view-product/:id" element={<ViewProduct />} />
        <Route path="/my-shopping-cart" element={<ShoppingCart />} />
        <Route path="/my-favourite-products" element={<Favorites />} />
        <Route path="/my-orders" element={<MyOrders />} />
        <Route path="/orders" element={<ALLOrders />} />


      </Routes>
    </Router>
  );
}

export default App;
