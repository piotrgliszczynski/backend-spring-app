import React from 'react';
import { fireEvent, render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import Heading from './Heading';
import { BrowserRouter } from 'react-router-dom';
import * as CustomerContext from './hooks/CustomerContext';
import * as AuthContext from './hooks/AuthContext';

jest.mock('./hooks/CustomerContext');
jest.mock('./hooks/AuthContext');

const CUSTOMER = {
  "id": 0,
  "name": "Mary Jackson",
  "email": "maryj@abc.com",
  "password": "maryj"
};

const EMPTY_CUSTOMER = {
  id: -1,
  name: "",
  email: "",
  password: ""
};

describe("Heading", () => {

  afterEach(() => {
    CustomerContext.useCustomer.mockReset();
    AuthContext.useAuth.mockReset();
  });

  it("Should have Home and Add links when customer is not selected", () => {
    // Given
    const homeLink = "Home";
    const formLink = "Add";

    const contextValues = {
      customer: EMPTY_CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER
    };
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementationOnce(() => contextValues);

    const authContextValues = {
      user: null,
      logout: jest.fn()
    };
    jest.spyOn(AuthContext, 'useAuth').mockImplementationOnce(() => authContextValues);

    render(
      <Heading />,
      { wrapper: BrowserRouter }
    );

    // When
    const homeLinkElement = screen.getByRole("link", { name: homeLink });
    const formLinkElement = screen.getByRole("link", { name: formLink });

    // Then
    expect(homeLinkElement).toBeInTheDocument();
    expect(formLinkElement).toBeInTheDocument();
  });

  it("Should have Home and Update links when customer is selected", () => {
    // Given
    const homeLink = "Home";
    const formLink = "Update";

    const contextValues = {
      customer: CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementationOnce(() => contextValues);

    const authContextValues = {
      user: null,
      logout: jest.fn()
    };
    jest.spyOn(AuthContext, 'useAuth').mockImplementationOnce(() => authContextValues);

    render(
      <Heading />,
      { wrapper: BrowserRouter }
    );

    // When
    const homeLinkElement = screen.getByRole("link", { name: homeLink });
    const formLinkElement = screen.getByRole("link", { name: formLink });

    // Then
    expect(homeLinkElement).toBeInTheDocument();
    expect(formLinkElement).toBeInTheDocument();
  });

  it("Should have Login link when customer is not logged in", () => {
    // Given
    const loginLink = "Login";

    const contextValues = {
      customer: EMPTY_CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementationOnce(() => contextValues);

    const authContextValues = {
      user: null,
      logout: jest.fn()
    };
    jest.spyOn(AuthContext, 'useAuth').mockImplementationOnce(() => authContextValues);

    render(
      <Heading />,
      { wrapper: BrowserRouter }
    );

    // When
    const loginLinkElement = screen.getByRole("link", { name: loginLink });

    // Then
    expect(loginLinkElement).toBeInTheDocument();
  });

  it("Should have Logout link when customer is logged in", () => {
    // Given
    const logoutLink = "Logout";

    const contextValues = {
      customer: EMPTY_CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementationOnce(() => contextValues);

    const authContextValues = {
      user: CUSTOMER,
      logout: jest.fn()
    };
    jest.spyOn(AuthContext, 'useAuth').mockImplementationOnce(() => authContextValues);

    render(
      <Heading />,
      { wrapper: BrowserRouter }
    );

    // When
    const logoutLinkElement = screen.getByRole("link", { name: logoutLink });

    // Then
    expect(logoutLinkElement).toBeInTheDocument();
  });

  it("Logout link should logout correctly", () => {
    // Given
    const logoutLink = "Logout";

    const contextValues = {
      customer: EMPTY_CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementationOnce(() => contextValues);

    const authContextValues = {
      user: CUSTOMER,
      logout: jest.fn()
    };
    jest.spyOn(AuthContext, 'useAuth').mockImplementationOnce(() => authContextValues);

    render(
      <Heading />,
      { wrapper: BrowserRouter }
    );

    // When
    const logoutLinkElement = screen.getByRole("link", { name: logoutLink });
    fireEvent.click(logoutLinkElement);

    // Then
    expect(authContextValues.logout).toHaveBeenCalledTimes(1);
  });

  it("Should not display greeting when user is not logged in", () => {
    // Given
    const greetingMessage = "Hello";
    const contextValues = {
      customer: EMPTY_CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementationOnce(() => contextValues);

    const authContextValues = {
      user: null,
      logout: jest.fn()
    };
    jest.spyOn(AuthContext, 'useAuth').mockImplementationOnce(() => authContextValues);

    render(
      <Heading />,
      { wrapper: BrowserRouter }
    );

    // When
    const greetingElement = screen.queryByText(greetingMessage, { exact: false });

    // Then
    expect(greetingElement).not.toBeInTheDocument();
  });

  it("Should have greeting when customer is logged in", () => {
    // Given
    const greetingMessage = "Hello";
    const contextValues = {
      customer: EMPTY_CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementationOnce(() => contextValues);

    const authContextValues = {
      user: CUSTOMER,
      logout: jest.fn()
    };
    jest.spyOn(AuthContext, 'useAuth').mockImplementationOnce(() => authContextValues);

    render(
      <Heading />,
      { wrapper: BrowserRouter }
    );

    // When
    const greetingElement = screen.getByText(greetingMessage, { exact: false });

    // Then
    expect(greetingElement).toBeInTheDocument();
  });
});