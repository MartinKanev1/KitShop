
import React, { useEffect, useState } from 'react';
import '../Styles/ProductCard.css';
import EditIcon from '@mui/icons-material/Edit';
import { Link } from 'react-router-dom';
import { Heart } from 'lucide-react';
import { AddToFavourites, RemoveFromFavourites, GetUserFavourites } from '../Services/FavouriteService';
import useUserInfo from '../hooks/useUserInfo'; 




const ProductCard = ({ product }) => {
  if (!product || !product.imageUrl) return null;
  

  const id = product.productKitId;

  const { user } = useUserInfo(); 

  const isOutOfStock = product.sizes?.every(size => size.quantity === 0);

  const [isFavorited, setIsFavorited] = useState(false);

  useEffect(() => {
    const fetchFavorites = async () => {
        try {
            const favorites = await GetUserFavourites();
            setIsFavorited(favorites.includes(id));
        } catch (error) {
            console.error("Error checking if offer is favorited:", error);
        }
    };

    fetchFavorites();
}, [id]);

const handleFavoriteToggle = async (e) => {
    e.stopPropagation(); 

   

    try {
        if (isFavorited) {
            const success = await RemoveFromFavourites(id);
            if (success) {
                setIsFavorited(false);
            }
        } else {
            const success = await AddToFavourites(id);
            if (success) {
                setIsFavorited(true);
            }
        }
    } catch (error) {
        console.error("Error toggling favorite:", error);
    }
};
  

  return (
    
    <div className="product-card">
      {user?.role === 'Admin' && (
       <Link to={`/edit-product/${product.productKitId}`} >
      <button className="edit-button">
        <EditIcon fontSize="small" />
      </button>
      </Link>
      )}

      <Heart
          onClick={handleFavoriteToggle}
          style={{
            position: "absolute",
            top: "10px",
            right: "10px",
            cursor: "pointer",
            background: "white",
            padding: "6px",
            borderRadius: "50%",
            boxShadow: "0px 0px 5px rgba(0,0,0,0.2)"
          }}
          color={isFavorited ? "red" : "black"}
          fill={isFavorited ? "red" : "none"}
        />

      {isOutOfStock && (
        <div className="out-of-stock-banner">Out of Stock</div>
      )}


      <Link to={`/view-product/${product.productKitId}`} className="product-card-link">

      <img
        src={product.imageUrl}
        alt={product.name}
        className="product-card-image"
      />

      <div className="product-card-info">
        <h1 className="product-card-title">{product.name}</h1>
        <p className="product-card-price">${product.price.toFixed(2)}</p>

        
      </div>
      </Link>
    </div>
  );
};

export default ProductCard;
