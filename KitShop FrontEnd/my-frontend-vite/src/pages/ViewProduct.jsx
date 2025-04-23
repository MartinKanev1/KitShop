import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import Header from '../components/Header';
import BuyNowModal from '../components/BuyNowModal';
import { getProductById, addReview, getAllReviewsForProduct } from '../Services/ProductKitService';
import { addProductToCart } from '../Services/ShoppingCartService';
import { buyProduct } from '../Services/OrderService';
import '../Styles/ViewProduct.css';



const ViewProduct = () => {
    const { id } = useParams(); 
    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(true);
    const [selectedSize, setSelectedSize] = useState('S');
    const [quantity, setQuantity] = useState(1);
    const [isBuyNowOpen, setIsBuyNowOpen] = useState(false);
    const [showReviewForm, setShowReviewForm] = useState(false);
    const [reviewText, setReviewText] = useState('');
    const [showReviews, setShowReviews] = useState(false);
    const [reviews, setReviews] = useState([]);


  
    useEffect(() => {
      const fetchProduct = async () => {
        try {
          const data = await getProductById(id);
          setProduct(data);
        } catch (error) {
          console.error('Failed to fetch product:', error);
        } finally {
          setLoading(false);
        }
      };
  
      fetchProduct();
    }, [id]);
  
    const handleSizeClick = (size) => {
      setSelectedSize(size);
    };


    const handleBuyConfirm = async ({ address }) => {
      const userId = localStorage.getItem('userId');
      console.log('🛒 Sending order with:', {
        userId,
        productId: product.productKitId,
        size: selectedSize,
        quantity,
        address
      });
    
      try {
        await buyProduct(userId, product.productKitId, selectedSize, quantity, address);
        alert('🎉 Order placed successfully!');
        setIsBuyNowOpen(false);
      } catch (error) {
        console.error(' Error placing order:', error);
        alert(' Failed to place order.');
      }
    };
    
    

    const handleAddToCart = async () => {
        const userId = localStorage.getItem('userId'); 
        if (!selectedSize) {
          alert('Please select a size before adding to cart.');
          return;
        }
      
        if (quantity <= 0) {
          alert('Quantity must be at least 1.');
          return;
        }

       
      
        try {
          await addProductToCart(userId, product.productKitId, selectedSize, quantity);
          alert(' Product added to cart!');
        } catch (error) {
          alert(' Failed to add product to cart');
        }
      };

      const handleBuyNow = () => {
        if (!selectedSize) {
          alert('Please select a size before buying.');
          return;
        }
      
        if (quantity <= 0) {
          alert('Please select at least 1 quantity.');
          return;
        }
        setIsBuyNowOpen(true);
      };
      

      const handleReviewSubmit = async () => {
        if (!reviewText.trim()) {
          alert('Please enter a review before submitting!');
          return;
        }
      
        try {
          await addReview(product.productKitId, reviewText);
          alert(' Review submitted successfully!');
          setReviewText('');
          setShowReviewForm(false);
        } catch (error) {
          alert(' Failed to submit review');
        }
      };


      const handleViewReviews = async () => {
        
        if (showReviews) {
          setShowReviews(false);
          return;
        }
      
        try {
          const data = await getAllReviewsForProduct(product.productKitId);
          setReviews(data);
          setShowReviews(true);
        } catch (error) {
          console.error("Error loading reviews:", error);
        }
      };
      
      
      
      
  
    const handleQuantityChange = (type) => {
      if (type === 'decrease' && quantity > 1) {
        setQuantity(quantity - 1);
      } else if (type === 'increase') {
        setQuantity(quantity + 1);
      }
    };
  
    if (loading) return <div>Loading...</div>;
    if (!product) return <div>Product not found</div>;
  
    return (
      <>
        <Header showButtons={true} />
        <div className="view-product-container">
          
          <div className="product-image">
            <img
              src={product.imageUrl}
              alt={product.name}
              style={{ maxWidth: '100%', maxHeight: '100%' }}
            />
          </div>
  
          
          <div className="product-details">
           

            <div><strong>Name:</strong> {product.name}</div>
            <div><strong>Description:</strong> {product.description}</div>
            <div><strong>Team:</strong> {product.teamNameOfKit} <strong>Player:</strong> {product.playerNameOnKit}</div>
            
            

            <div><strong>Price:</strong> ${product.price}</div>
           

            <div className="selection-container">
            
            <div className="size-section">
              <label>Availlabel Sizes:</label>
              <div className="size-options">
                
                {product.sizes.map(({ size, quantity }) => (
  <button
    key={size}
    className={`size-button ${selectedSize === size ? 'selected' : ''}`}
    onClick={() => handleSizeClick(size)}
    disabled={quantity === 0}
    title={quantity === 0 ? 'Out of stock' : ''}
  >
    {size}
  </button>
))}

              </div>
            </div>
            
            
            <div className="quantity-section">
              <label>Quantity</label>
              <div className="quantity-box">
                <button onClick={() => handleQuantityChange('decrease')}>-</button>
                <span>{quantity}</span>
                <button onClick={() => handleQuantityChange('increase')}>+</button>
              </div>
            </div>
            </div>
            <div className="action-buttons">
  <button className="add-to-cart-button" onClick={handleAddToCart}>
    🛒 Add to Cart
  </button>

  <button className="buy-now-button" onClick={handleBuyNow} >
    💳 Buy Now
  </button>

  <div className="review-buttons">
    <button className="review-button" onClick={() => setShowReviewForm(!showReviewForm)} >
      ✍️ Add Review
    </button>
    <button className="review-button" onClick={handleViewReviews} >
    {showReviews ? " Hide Reviews" : " View All Reviews"}
    </button>
  </div>
</div>


          </div>
        </div>

        
    {showReviewForm && (
      <div className="review-form">
        <h3>📝 Leave a Review</h3>
        <textarea
          value={reviewText}
          onChange={(e) => setReviewText(e.target.value)}
          placeholder="Write your thoughts about this product..."
        />
        <button onClick={handleReviewSubmit}>Submit Review</button>
      </div>
    )}


{showReviews && reviews.length > 0 && (
  <div className="all-reviews-container">
    <h3>📚 All Reviews</h3>
    {reviews.map((review, index) => (
      <div key={index} className="single-review">
        <p>{review.comment}</p>
        <hr />
      </div>
    ))}
  </div>
)}

{showReviews && reviews.length === 0 && (
  <div className="all-reviews-container">
    <p>No reviews yet for this product.</p>
  </div>
)}


        <BuyNowModal
      isOpen={isBuyNowOpen}
      onClose={() => setIsBuyNowOpen(false)}
      onConfirm={handleBuyConfirm}
      product={product}
      size={selectedSize}
      quantity={quantity}
    />
     

     

    
      </>
    );
  };
  
  export default ViewProduct;
