import React from "react";
import { Link } from "react-router-dom";
import "./styles/Heading.css";
import { useCustomer } from "./hooks/CustomerContext";
import { useAuth } from "./hooks/AuthContext";

const Heading = () => {

  const { customer, emptyCustomer } = useCustomer();
  const { user, logout } = useAuth();

  const formLinkTitle = () =>
    customer.id === emptyCustomer.id ?
      'Add' : 'Update';

  const loginButton = () => {
    return !user ?
      <Link className="heading-button" to="/login">Login</Link> :
      <Link className="heading-button" to="/" onClick={logout}>Logout</Link>;
  }

  const registerButton = () => {
    return !user && <Link className="heading-button" to="/register">Register</Link>;
  }

  const greetingMessage = () => {
    return !user?.name ?
      '' : `Hello, ${user.name}!`;
  }

  return (
    <div className="header-container">
      <Link reloadDocument className="heading-title" to="/">Customers App</Link>
      <span>
        <span className="greeting-message">{greetingMessage()}</span>
        <Link reloadDocument className="heading-button" to="/">Home</Link>
        <Link className="heading-button" to="/customer-form">{formLinkTitle()}</Link>
        <Link className="heading-button" to="/events">Events</Link>
        {loginButton()}
        {registerButton()}
      </span>
    </div>
  )
}

export default Heading;