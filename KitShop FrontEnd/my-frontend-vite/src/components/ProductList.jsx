import React, { useEffect, useState } from 'react';
import ProductCard from '../components/ProductCard';
import '../Styles/ProductList.css';
import { getAllProducts,searchProductKits  } from '../Services/ProductKitService';
import { Button } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';


const ProductList = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedSize, setSelectedSize] = useState('');
  const [selectedClubType, setSelectedClubType] = useState('');
  const [allProducts, setAllProducts] = useState([])


  useEffect(() => {
    const fetchProducts = async () => {
      try {
        if (searchTerm.trim() === '') {
        const data = await getAllProducts();
        setProducts(data);
        setAllProducts(data);
        }
      } catch (err) {
        setError('Failed to load products');
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, [searchTerm]);

  


  const handleFilter = () => {
    let filtered = [...allProducts];
  
    if (selectedClubType) {
      filtered = filtered.filter(product => product.type === selectedClubType);
    }
  
    if (selectedSize) {
      filtered = filtered.filter(product =>
        product.sizes?.some(size =>
          size.size === selectedSize && size.quantity > 0
        )
      );
    }
  
    setProducts(filtered);
  };
  
  
  

  const handleSearch = async () => {
      
    try {
      const result = await searchProductKits(searchTerm);
      setProducts(result);
    } catch (err) {
      console.error('Search failed:', err);
    }
  };

  if (loading) return <div className="loading">Loading...</div>;
  if (error) return <div className="error">{error}</div>;



return (
  <>
    
    <div className="sticky-wrapper">
      <div className="search-bar-container">
        <input
          type="text"
          placeholder="Search kits by player or team name..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="search-input"
        />
        <Button
          variant="contained"
          onClick={handleSearch}
          sx={{
            marginLeft: '10px',
            padding: '10px 16px',
            backgroundColor: 'lightblue',
            color: 'black',
            '&:hover': {
              backgroundColor: '#add8e6',
            },
            minWidth: '48px',
          }}
        >
          <SearchIcon />
        </Button>
      </div>

      <div className="filter-bar">
        <select
          value={selectedSize}
          onChange={(e) => setSelectedSize(e.target.value)}
          className="filter-select"
        >
          <option value="">All Sizes</option>
          <option value="XS">XS</option>
          <option value="S">S</option>
          <option value="M">M</option>
          <option value="L">L</option>
          <option value="XL">XL</option>
        </select>

        <select
          value={selectedClubType}
          onChange={(e) => setSelectedClubType(e.target.value)}
          className="filter-select"
        >
          <option value="">All Club Types</option>
          <option value="TeamClub">Club</option>
          <option value="NationalClub">National</option>
        </select>

        <Button
          variant="contained"
          onClick={handleFilter}
          sx={{
            backgroundColor: 'lightblue',
            color: 'black',
            '&:hover': { backgroundColor: '#add8e6' },
          }}
        >
          Filter
        </Button>
      </div>
    </div>

    
    <div className="product-page-container">
      <div className="product-list">
        {products.length > 0 ? (
          products.map((product) => (
            <ProductCard key={product.productKitId} product={product} />
          ))
        ) : (
          <div className="no-products">No products found</div>
        )}
      </div>
    </div>
  </>
)};


export default ProductList;

