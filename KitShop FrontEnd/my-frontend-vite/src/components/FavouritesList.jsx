import React, { useEffect, useState } from 'react';
import { GetUserFavourites } from '../Services/FavouriteService';
import ProductCard from '../components/ProductCard';
import { getProductById } from '../Services/ProductKitService';
import '../Styles/FavouriteList.css'; 



const FavouriteList = () => {
  const [favourites, setFavourites] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchFavourites = async () => {
      try {
        const productIds = await GetUserFavourites();
        if (!productIds || productIds.length === 0) {
            setFavourites([]); 
            return;
        }
            const productPromises = productIds.map(id => getProductById(id));
            const productData = await Promise.all(productPromises);

            setFavourites(productData);
        
      } catch (error) {
        console.error('Failed to fetch favourites:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchFavourites();
  }, []);

  if (loading) return <p>Loading your favourites...</p>;

  if (favourites.length === 0) return <p>You have no favourite products yet.</p>;

  return (
    <div className="favourite-list">
      {favourites.map((product) => (
        <ProductCard key={product.productKitId} product={product} />
      ))}
    </div>
  );
};

export default FavouriteList;
