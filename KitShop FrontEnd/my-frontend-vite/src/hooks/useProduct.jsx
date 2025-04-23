import { useState, useEffect } from "react";
import { getProductById } from "../Services/ProductKitService";

const useProduct = (productKitId) => {
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProduct = async () => {
      setLoading(true);
      try {
        const data = await getProductById(productKitId);
        setProduct(data);
      } catch (err) {
        setError(err);
      } finally {
        setLoading(false);
      }
    };

    if (productKitId) {
      fetchProduct();
    }
  }, [productKitId]);

  return { product, loading, error };
};

export default useProduct;
