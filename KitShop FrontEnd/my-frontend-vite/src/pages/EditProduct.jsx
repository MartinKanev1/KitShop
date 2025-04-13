import React, { useEffect, useState } from 'react';
import Header from '../components/Header';
import { getProductById, updateProduct } from '../Services/ProductKitService';
import { useParams } from 'react-router-dom';
import '../Styles/AddProduct.css';

const EditProduct = () => {
  const { id } = useParams();
  const [imagePreview, setImagePreview] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [originalData, setOriginalData] = useState(null);
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    price: '',
    playerNameOnKit: '',
    teamNameOfKit: '',
    type: '',
    sizes: [{ size: '', quantity: '' }],
    imageFile: null,
  });

  useEffect(() => {
    console.log("Component mounted. Fetching product ID:", id); 
    const fetchProduct = async () => {
      try {
        const product = await getProductById(id);
        console.log("Fetched product:", product); 
        setFormData({
          ...product,
          imageFile: null,
        });
        setOriginalData(product);
        setImagePreview(product.imageUrl);
      } catch (error) {
        console.error('❌ Failed to fetch product:', error); 
      }
    };
  
    fetchProduct();
  }, [id]);
  

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setFormData({ ...formData, imageFile: file });
      const imageURL = URL.createObjectURL(file);
      setImagePreview(imageURL);
    }
  };

  const handleInputChange = (e) => {
    if (!isEditing) return;
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSizeChange = (index, field, value) => {
    if (!isEditing) return;
    const updatedSizes = [...formData.sizes];
    updatedSizes[index][field] = value;
    setFormData({ ...formData, sizes: updatedSizes });
  };

  const addSizeField = () => {
    if (!isEditing) return;
    setFormData((prev) => ({
      ...prev,
      sizes: [...prev.sizes, { size: '', quantity: '' }],
    }));
  };

  const handleSaveChanges = async (e) => {
    e.preventDefault();

    try {
      const result = await updateProduct(id, formData);
      alert('Product updated successfully!');
      console.log(result);
      setIsEditing(false);
      setOriginalData(result);
    } catch (err) {
      alert('Failed to update product. Check console.');
      console.error(err);
    }
  };

  const handleCancelEdit = () => {
    setFormData({ ...originalData, imageFile: null });
    setImagePreview(originalData.imageUrl);
    setIsEditing(false);
  };

  return (
    <>
      <Header showButtons={true} />
      <form className="add-product-container" onSubmit={handleSaveChanges}>
        <div className="image-upload-section">
          <div className="image-box-product">
            {imagePreview ? (
              <img src={imagePreview} alt="Preview" className="preview-img" />
            ) : (
              <p>image here</p>
            )}
          </div>
          <input
            type="file"
            accept="image/*"
            className="file-input"
            onChange={handleImageChange}
            disabled={!isEditing}
          />
        </div>

        <div className="product-details-section">
          <label>
            name:
            <input type="text" name="name" value={formData.name} onChange={handleInputChange} readOnly={!isEditing} />
          </label>
          <label>
            description:
            <textarea name="description" value={formData.description} onChange={handleInputChange} readOnly={!isEditing} />
          </label>
          <label>
            price:
            <input type="number" name="price" value={formData.price} onChange={handleInputChange} readOnly={!isEditing} />
          </label>
          <label>
            player name of kit:
            <input type="text" name="playerNameOnKit" value={formData.playerNameOnKit} onChange={handleInputChange} readOnly={!isEditing} />
          </label>
          <label>
            team name on kit:
            <input type="text" name="teamNameOfKit" value={formData.teamNameOfKit} onChange={handleInputChange} readOnly={!isEditing} />
          </label>
          <label>
            Club Type:
            <select name="type" value={formData.type} onChange={handleInputChange} disabled={!isEditing}>
              <option value="">Select</option>
              <option value="NationalClub">NationalClub</option>
              <option value="TeamClub">TeamClub</option>
            </select>
          </label>

          <div className="sizes-section">
            <p>Sizes:</p>
            {formData.sizes.map((sizeEntry, index) => (
              <div key={index} className="size-entry">
                <input
                  type="text"
                  placeholder="Size"
                  value={sizeEntry.size}
                  onChange={(e) => handleSizeChange(index, 'size', e.target.value)}
                  readOnly={!isEditing}
                />
                <input
                  type="number"
                  placeholder="Quantity"
                  value={sizeEntry.quantity}
                  onChange={(e) => handleSizeChange(index, 'quantity', e.target.value)}
                  readOnly={!isEditing}
                />
              </div>
            ))}
            {isEditing && (
              <button type="button" onClick={addSizeField}>
                + Add Size
              </button>
            )}
          </div>

          {!isEditing ? (
            <button type="button" className="submit-button" onClick={() => setIsEditing(true)}>
              Edit
            </button>
          ) : (
            <>
              <button type="submit" className="submit-button">Save Changes</button>
              <button type="button" className="submit-button" onClick={handleCancelEdit}>
                Cancel
              </button>
            </>
          )}
        </div>
      </form>
    </>
  );
};

export default EditProduct;
