import { useAuth } from './AuthContext';
import { fetchWithAuth } from './Interceptor';

export const useFetchWithAuth = () => {
  const { tokens, refreshAccessToken, isTokenExpired } = useAuth();

  const fetchWithAuthWrapper = async (url, options = {}) => {
    // console.log(tokens);
    return await fetchWithAuth(url, options, tokens, refreshAccessToken, isTokenExpired);
  };

  return fetchWithAuthWrapper;
};
