import React, { useState } from 'react';
import Header from '../components/Header';
import { addProduct } from '../Services/ProductKitService';
import '../Styles/AddProduct.css';

const AddProduct = () => {
  const [imagePreview, setImagePreview] = useState(null);
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

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setFormData({ ...formData, imageFile: file });
      const imageURL = URL.createObjectURL(file);
      setImagePreview(imageURL);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSizeChange = (index, field, value) => {
    const updatedSizes = [...formData.sizes];
    updatedSizes[index][field] = value;
    setFormData({ ...formData, sizes: updatedSizes });
  };

  const addSizeField = () => {
    setFormData((prev) => ({
      ...prev,
      sizes: [...prev.sizes, { size: '', quantity: '' }],
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    try {
      const result = await addProduct(formData);
      alert('Product added successfully!');
      console.log(result);
      
    } catch (err) {
      alert('Failed to add product. Check console.');
      console.error(err);
    }
  };

  return (
    <>
      <Header showButtons={true} />
      <form className="add-product-container" onSubmit={handleSubmit}>
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
          />
        </div>

        <div className="product-details-section">
          <label>
            name:
            <input type="text" name="name" value={formData.name} onChange={handleInputChange} />
          </label>
          <label>
            description:
            <textarea name="description" value={formData.description} onChange={handleInputChange} />
          </label>
          <label>
            price:
            <input type="number" name="price" value={formData.price} onChange={handleInputChange} />
          </label>
          <label>
            player name of kit:
            <input type="text" name="playerNameOnKit" value={formData.playerNameOnKit} onChange={handleInputChange} />
          </label>
          <label>
            team name on kit:
            <input type="text" name="teamNameOfKit" value={formData.teamNameOfKit} onChange={handleInputChange} />
          </label>
          <label>
            Club Type:
            <select name="type" value={formData.type} onChange={handleInputChange}>
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
                />
                <input
                  type="number"
                  placeholder="Quantity"
                  value={sizeEntry.quantity}
                  onChange={(e) => handleSizeChange(index, 'quantity', e.target.value)}
                />
              </div>
            ))}
            <button type="button" onClick={addSizeField}>
              + Add Size
            </button>
          </div>

          
          <button type="submit" className="submit-button">Submit</button>
        </div>
      </form>
    </>
  );
};

export default AddProduct;
