import { useState, useEffect } from 'react';
import { getUserInfo } from '../Services/UserService';

const useUserInfo = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const userData = await getUserInfo();
        setUser(userData);
      } catch (err) {
        setError(err);
        console.error("Failed to load user data:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, []);

  return { user, setUser, loading, error };
};

export default useUserInfo;
