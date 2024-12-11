
import React, { createContext, useState, useContext, useEffect, useCallback } from 'react';
import {jwtDecode} from 'jwt-decode';
import api from '../services/Api';
import { handleError } from '../utils/ErrorHandler';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [tokens, setTokens] = useState({
    accessToken: localStorage.getItem('access_token'),
    refreshToken: localStorage.getItem('refresh_token'),
  });
  const [userInfo, setUserInfo] = useState(null);

 // Salva i token e aggiorna il contesto
  const saveTokens = useCallback((accessToken, refreshToken) => {
    localStorage.setItem('access_token', accessToken);
    localStorage.setItem('refresh_token', refreshToken);
    setTokens({ accessToken, refreshToken });
    setUserInfo(decodeToken(accessToken));
  }, []);


  // Cancella i token e le informazioni utente
  const clearTokens = useCallback(() => {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    setTokens({ accessToken: null, refreshToken: null });
    setUserInfo(null);
    localStorage.clear();
    sessionStorage.clear();
  }, []);

  // Decodifica il token per ottenere informazioni utente
  const decodeToken = (token) => {
    try {
      return jwtDecode(token);
    } catch (error) {
      console.error('Errore nella decodifica del token:', error);
      return null;
    }
  };

  // Verifica se il token è scaduto
  // const isTokenExpired = (token) => {
  //   const decoded = decodeToken(token);
  //   if (!decoded) return true;
  //   return Date.now() / 1000 > decoded.exp;
  // };


  const isTokenExpired = useCallback((token) => {
    const decoded = decodeToken(token);
    if (!decoded) return true;
    return Date.now() / 1000 > decoded.exp;
  },[]);

  // Refresh del token
  const refreshAccessToken = useCallback(async () => {
    const refreshToken = tokens.refreshToken;
    if (!refreshToken) {
      handleError(new Error('Refresh token non disponibile.'));
      clearTokens();
      return;
    }

    try {
      const newTokens = await api.refreshToken(refreshToken);
      saveTokens(newTokens.access_token, newTokens.refresh_token);
    } catch (error) {
      console.error('Errore durante il refresh del token:', error);
      clearTokens();
      throw error;
    }
  }, [tokens.refreshToken, clearTokens, saveTokens]);

 



  const fetchUserInfo = useCallback(async () => {
    // console.log('Esecuzione fetchUserInfo...');
    // console.log('Access Token Disponibile:', tokens.accessToken);
  
    if (!tokens.accessToken) {
      console.error('Token di accesso non disponibile.');
      return null;
    }
  
    try {
      const userInfo = await api.getUserInfo(tokens.accessToken);
      // console.log('Informazioni utente recuperate:', userInfo);
      setUserInfo(userInfo);
      return userInfo;
    } catch (error) {
      console.error('Errore nel recupero delle informazioni utente:', error);
      clearTokens();
      throw error;
    }
  }, [tokens.accessToken, clearTokens]);
  
  const [message, setMessage] = useState("");
  const clearMessage = () => setMessage("");


  //pensiamo a come fare , ricorda non è compilato.
  // const logout = useCallback(async () => {
  //   try {
  //     // Logout logica
  //   } catch (error) {
  //     console.error("Errore durante il logout:", error);
  //   } finally {
  //     setMessage("Logout eseguito con successo!");
  //     clearTokens();
  //   }
  // }, [clearTokens]);


// senza refresh token grazie al cazzo che non mi va l'end point di end session....


const getUserId = useCallback(() => {
  if (userInfo?.userId) {
    return userInfo.userId;
  }

  const decoded = decodeToken(tokens.accessToken);
  return decoded?.userId; // Cerca `userId` come attributo personalizzato nel token
}, [userInfo, tokens.accessToken]);
  
  
    
   
 
  // Logout
  const logout = useCallback(async () => {
    try {
       if (tokens.refreshToken) {
        // console.log(tokens.refreshToken);
        await api.logout(tokens.refreshToken);
      }
    } catch (error) {
      console.error('Errore durante il logout:', error);
      return false;
    } finally {
      clearTokens();
      setMessage("Logout eseguito con successo!");
      document.cookie.split(";").forEach((cookie) => {
        const eqPos = cookie.indexOf("=");
        const name = eqPos > -1 ? cookie.substring(0, eqPos) : cookie;
        document.cookie = `${name}=;expires=Thu, 01 Jan 1970 00:00:00 UTC;path=/;`;
      });
    
      // Reindirizza alla pagina di login
      // window.location.href = "/login";
      return true;
    }
  }, [clearTokens,tokens.refreshToken]);



  // Aggiorna i token all'inizio
  useEffect(() => {
    const storedAccessToken = localStorage.getItem('access_token');
    const storedRefreshToken = localStorage.getItem('refresh_token');

    if (storedAccessToken && storedRefreshToken) {
      if (isTokenExpired(storedAccessToken)) {
        refreshAccessToken();
      } else {
        setTokens({ accessToken: storedAccessToken, refreshToken: storedRefreshToken });
        setUserInfo(decodeToken(storedAccessToken));
        fetchUserInfo();
      }
    }
  }, [refreshAccessToken, fetchUserInfo,isTokenExpired]);

  return (
    <AuthContext.Provider
      value={{
        tokens,
        userInfo,
        saveTokens,
        clearTokens,
        refreshAccessToken,
        fetchUserInfo,
        logout,
        message,
        setMessage,
        clearMessage,
        isTokenExpired,
        getUserId,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
