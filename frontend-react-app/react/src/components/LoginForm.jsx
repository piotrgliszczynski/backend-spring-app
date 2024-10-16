import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import './styles/LoginForm.css';
import { useAuth } from './hooks/AuthContext';

const LoginForm = () => {

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const { user, login } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();

  const onLogin = (event) => {
    event.preventDefault();
    login(username, password);
  }

  const onChange = (field, event) => {
    switch (field) {
      case 'username':
        setUsername(event.target.value);
        break;
      case 'password':
        setPassword(event.target.value);
        break;
    }
  }

  useEffect(() => {
    if (!user) {
      return;
    }

    if (location?.state?.from?.pathname) {
      navigate(location.state.from.pathname);
      return;
    }

    navigate('/');
  }, [user])

  return (
    <div className="login-form">
      <h2 className="login-title">Login</h2>
      <form className="login-form-parent" onSubmit={onLogin}>
        <label htmlFor="username">Username:</label>
        <input type="text" id="username" placeholder="Enter username"
          onChange={(event) => onChange('username', event)} value={username}></input>

        <label htmlFor="password">Password:</label>
        <input type="password" id="password" placeholder="Enter password"
          onChange={(event) => onChange('password', event)} value={password}></input>

        <div className="login-button">
          <button className="btn" type="submit">Login</button>
        </div>
      </form>
    </div>
  )
}

export default LoginForm;