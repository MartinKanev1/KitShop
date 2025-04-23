import React, { useEffect, useState } from 'react';
import Header from '../components/Header';
import '../Styles/ShoppingCart.css'; 
import { getCartItems, getProductIdByCartItem, removeProductFromCart } from '../Services/ShoppingCartService';
import { getProductById } from '../Services/ProductKitService';
import CheckoutModal from '../components/CheckoutModal';
import { checkoutOrder } from '../Services/OrderService';
import { Link } from 'react-router-dom';




const ShoppingCart = () => {
    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);
  
    const userId = localStorage.getItem('userId'); 
    

    const [showCheckout, setShowCheckout] = useState(false);
        

    useEffect(() => {
        const fetchCart = async () => {
          try {
            const items = await getCartItems(userId);
      
            const enrichedItems = await Promise.all(
              items.map(async (item) => {
                const productId = await getProductIdByCartItem(item.cartItemId);
                const product = await getProductById(productId);
      
                return {
                  ...item,
                  productId,
                  name: product.name,
                  imageUrl: product.imageUrl,
                  price: product.price,
                };
              })
            );
      
            console.log('Fully enriched cart items:', enrichedItems);
            setCartItems(enrichedItems);
          } catch (error) {
            console.error('Failed to fetch cart data:', error);
          } finally {
            setLoading(false);
          }
        };
      
        if (userId) {
          fetchCart();
        }
      }, [userId]);
      
      
    


    const handleCheckoutConfirm = async ({ address }) => {
        try {
          const orderData = await checkoutOrder(userId, address);
          console.log('Order placed successfully:', orderData);
          setCartItems([]);
          setShowCheckout(false);
          alert(' Order placed successfully!');
        } catch (error) {
          const message =
            error?.response?.data?.message ||
            error?.response?.data ||
            'Something went wrong with checkout.';
      
          alert(` Checkout failed: ${message}`);
        }
      };
      
      

  
      const handleDelete = async (cartItemId) => {
        try {
          await removeProductFromCart(userId, cartItemId);
          setCartItems((prevItems) => prevItems.filter(item => item.cartItemId !== cartItemId));
        } catch (error) {
          console.error('Failed to remove item from cart:', error);
        }
      };
      
  
    const total = cartItems.reduce((acc, item) => acc + Number(item.price) * item.quantity, 0).toFixed(2);

    

  
    return (
      <>
        <Header showButtons={true} />
        <div className="shopping-cart-container">
          <h2 className="cart-title">Shopping Cart</h2>
          {loading ? (
            <p>Loading...</p>
          ) : cartItems.length === 0 ? (
            <p className="empty-message">Your cart is empty.</p>
          ) : (
            <div className="cart-items">
              
              

{cartItems.map((item) => (

<div key={item.cartItemId} className="cart-item">
  <Link to={`/view-product/${item.productId}`} className="cart-item-link">
    <img src={item.imageUrl} alt={item.name} className="item-image" />
    <div className="item-details">
      <h3 className="item-name">{item.name}</h3>
      <p className="item-meta">Size: {item.size}  ||   Quantity: {item.quantity}</p>
      
    </div>
  </Link>

  <div className="item-side">
    <p className="item-price">${Number(item.price * item.quantity).toFixed(2)}</p>
    <button
      onClick={(e) => {
        e.stopPropagation(); 
        handleDelete(item.cartItemId);
      }}
      className="remove-button"
    >
      Remove
    </button>
  </div>
</div>

))}


              <div className="cart-footer">
                <span className="cart-total">Total: ${total}</span>
                <button className="checkout-button" onClick={() => setShowCheckout(true)}>
  Checkout
</button>

              </div>
            </div>
          )}
        </div>

        <CheckoutModal
  isOpen={showCheckout}
  onClose={() => setShowCheckout(false)}
  onConfirm={handleCheckoutConfirm}
  cartItems={cartItems}
/>

      </>
    );
  };
  
  export default ShoppingCart;
