import React, { createContext, useContext, useState } from 'react';
import { getToken } from '../../rest/authapi.js';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  const login = async (username, password) => {
    const token = await getToken({
      email: username,
      password: password
    });

    if (token) {
      setUser(token);
    } else {
      alert('Login failed');
    }
  }

  const logout = () => {
    setUser(null);
  }

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
};

export const useAuth = () => useContext(AuthContext);