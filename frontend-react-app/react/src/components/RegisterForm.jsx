import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { register } from '../rest/authapi';
import './styles/RegisterForm.css';

const RegisterForm = () => {

  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const onRegister = async (event) => {
    event.preventDefault();
    const response = await register({
      name: username,
      email: email,
      password: password
    });

    alert(`Registered new user for email ${response.email}`);
    navigate('/');
  }

  const onChange = (field, event) => {
    switch (field) {
      case 'username':
        setUsername(event.target.value);
        break;
      case 'password':
        setPassword(event.target.value);
        break;
      case 'email':
        setEmail(event.target.value);
        break;
    }
  }

  return (
    <div className="register-form">
      <h2 className="register-title">Register</h2>
      <form className="register-form-parent" onSubmit={onRegister}>
        <label htmlFor="username">Username:</label>
        <input type="text" id="username" placeholder="Enter username"
          onChange={(event) => onChange('username', event)} value={username}></input>

        <label htmlFor="email">Email:</label>
        <input type="email" id="email" placeholder="Enter email"
          onChange={(event) => onChange('email', event)} value={email}></input>

        <label htmlFor="password">Password:</label>
        <input type="password" id="password" placeholder="Enter password"
          onChange={(event) => onChange('password', event)} value={password}></input>

        <div className="register-button">
          <button className="btn" type="submit">Register</button>
        </div>
      </form>
    </div>
  )
}

export default RegisterForm;