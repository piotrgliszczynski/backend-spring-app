import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from './hooks/AuthContext';

const RequireAuth = ({ children }) => {
  const location = useLocation();
  const auth = useAuth();

  if (!auth?.user) {
    return (
      <Navigate to="/login" state={{ from: location }} replace />
    );
  }

  return children;
}

export default RequireAuth;