import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import { Router, BrowserRouter } from 'react-router-dom';
import { createMemoryHistory } from 'history';
import LoginForm from './LoginForm';
import * as AuthContext from './hooks/AuthContext';
import * as ReactRouter from 'react-router';

jest.mock('./hooks/AuthContext');

const CUSTOMER = {
  "id": 0,
  "name": "Mary Jackson",
  "email": "maryj@abc.com",
  "password": "maryj"
};

describe('Login form', () => {

  const loginContext = {
    user: CUSTOMER,
    login: jest.fn()
  }

  const mockNavigate = jest.fn();

  beforeEach(() => {
    jest.spyOn(AuthContext, 'useAuth').mockImplementation(() => loginContext);
    jest.spyOn(ReactRouter, 'useNavigate').mockImplementation(() => mockNavigate);
  })

  afterEach(() => {
    AuthContext.useAuth.mockReset();
    loginContext.login.mockReset();
    ReactRouter.useNavigate.mockReset();
  });

  it('Should have title, username, password and Login button elements', () => {
    // Given
    const title = 'Login';
    const usernameLabel = 'Username:';
    const usernamePlaceholder = 'Enter username';
    const passwordLabel = 'Password:';
    const passwordPlaceholder = 'Enter password';
    const buttonText = 'Login';

    render(
      <LoginForm />,
      { wrapper: BrowserRouter }
    );

    // When
    const titleElement = screen.getByRole('heading', { name: title });
    const usernameLabelElement = screen.getByText(usernameLabel);
    const usernameElement = screen.getAllByPlaceholderText(usernamePlaceholder);
    const passwordLabelElement = screen.getByText(passwordLabel);
    const passwordElement = screen.getByPlaceholderText(passwordPlaceholder);
    const buttonElement = screen.getByRole('button', { name: buttonText });

    // Then
    expect(titleElement).toBeInTheDocument;
    expect(usernameLabelElement).toBeInTheDocument;
    expect(usernameElement).toBeInTheDocument;
    expect(passwordLabelElement).toBeInTheDocument;
    expect(passwordElement).toBeInTheDocument;
    expect(buttonElement).toBeInTheDocument;
  });

  it('Should be able to enter values', async () => {
    // Given
    const usernamePlaceholder = 'Enter username';
    const passwordPlaceholder = 'Enter password';
    const username = 'test@test.com';
    const password = '12345';

    render(
      <LoginForm />,
      { wrapper: BrowserRouter }
    );

    const usernameElement = screen.getByPlaceholderText(usernamePlaceholder);
    const passwordElement = screen.getByPlaceholderText(passwordPlaceholder);

    // When
    fireEvent.change(usernameElement, { target: { value: username } });
    fireEvent.change(passwordElement, { target: { value: password } });

    const changedUsername = await screen.findByDisplayValue(username);
    const changedPassword = await screen.findByDisplayValue(password);

    // Then
    expect(changedUsername).toBeInTheDocument();
    expect(changedPassword).toBeInTheDocument();
  });

  it('Should be able to login', () => {
    // Given
    const usernamePlaceholder = 'Enter username';
    const passwordPlaceholder = 'Enter password';
    const buttonText = 'Login';
    const username = 'test@test.com';
    const password = '12345';
    render(
      <LoginForm />,
      { wrapper: BrowserRouter }
    );

    const usernameElement = screen.getByPlaceholderText(usernamePlaceholder);
    const passwordElement = screen.getByPlaceholderText(passwordPlaceholder);
    const buttonElement = screen.getByRole('button', { name: buttonText });

    // When
    fireEvent.change(usernameElement, { target: { value: username } });
    fireEvent.change(passwordElement, { target: { value: password } });
    fireEvent.click(buttonElement);

    // Then
    expect(loginContext.login).toHaveBeenCalledTimes(1);
    expect(loginContext.login.mock.calls[0][0]).toEqual(username);
    expect(loginContext.login.mock.calls[0][1]).toEqual(password);
  });

  it('Should navigate to form after login', async () => {
    // Given
    const usernamePlaceholder = 'Enter username';
    const passwordPlaceholder = 'Enter password';
    const buttonText = 'Login';
    const username = CUSTOMER.email;
    const password = CUSTOMER.password;

    const history = createMemoryHistory();
    const state = { from: { pathname: '/test' } }
    history.push("/login", state);

    render(
      <Router location={history.location} >
        <LoginForm />
      </Router>
    );

    const usernameElement = screen.getByPlaceholderText(usernamePlaceholder);
    const passwordElement = screen.getByPlaceholderText(passwordPlaceholder);
    const buttonElement = screen.getByRole('button', { name: buttonText });

    // When
    fireEvent.change(usernameElement, { target: { value: username } });
    fireEvent.change(passwordElement, { target: { value: password } });
    fireEvent.click(buttonElement);

    // Then
    expect(loginContext.login).toHaveBeenCalledTimes(1);
    expect(loginContext.login.mock.calls[0][0]).toEqual(username);
    expect(loginContext.login.mock.calls[0][1]).toEqual(password);
    expect(mockNavigate).toHaveBeenCalled();
    expect(mockNavigate).toHaveBeenCalledWith("/test");
  });

  it('Should navigate to main page after login', async () => {
    // Given
    const usernamePlaceholder = 'Enter username';
    const passwordPlaceholder = 'Enter password';
    const buttonText = 'Login';
    const username = CUSTOMER.email;
    const password = CUSTOMER.password;

    const history = createMemoryHistory();
    history.push("/login");

    render(
      <Router location={history.location} >
        <LoginForm />
      </Router>
    );

    const usernameElement = screen.getByPlaceholderText(usernamePlaceholder);
    const passwordElement = screen.getByPlaceholderText(passwordPlaceholder);
    const buttonElement = screen.getByRole('button', { name: buttonText });

    // When
    fireEvent.change(usernameElement, { target: { value: username } });
    fireEvent.change(passwordElement, { target: { value: password } });
    fireEvent.click(buttonElement);

    // Then
    expect(loginContext.login).toHaveBeenCalledTimes(1);
    expect(loginContext.login.mock.calls[0][0]).toEqual(username);
    expect(loginContext.login.mock.calls[0][1]).toEqual(password);
    expect(mockNavigate).toHaveBeenCalled();
    expect(mockNavigate).toHaveBeenCalledWith("/");
  });
})