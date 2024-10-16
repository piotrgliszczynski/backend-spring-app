import React, { useEffect } from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import * as RestApi from '../../rest/restdb';
import { AuthProvider, useAuth } from './AuthContext';

jest.mock('../../rest/restdb');

const CUSTOMER = {
  "id": 0,
  "name": "Mary Jackson",
  "email": "maryj@abc.com",
  "password": "maryj"
};

const WRONG_PASSWORD = {
  "id": 0,
  "name": "Mary Jackson",
  "email": "maryj@abc.com",
  "password": "maryj2"
};

const TestLogin = () => {
  const { user, login } = useAuth();

  useEffect(() => {
    login(CUSTOMER.email, CUSTOMER.password);
  }, []);

  return (
    <div>
      {JSON.stringify(user)}
    </div>
  )
}

const TestLogout = () => {
  const { user, login, logout } = useAuth();

  useEffect(() => {
    login(CUSTOMER.email, CUSTOMER.password);
    logout();
  }, []);

  return (
    <div>
      {JSON.stringify(user)}
    </div>
  )
}

describe('Auth Context', () => {

  beforeEach(() => {
    jest.spyOn(window, 'alert').mockImplementation();
  })

  afterEach(() => {
    RestApi.getByEmail.mockReset();
    window.alert.mockReset();
  })

  it('Should set user when login successfull', async () => {
    // Given
    jest.spyOn(RestApi, 'getByEmail').mockResolvedValue(CUSTOMER);

    render(
      <TestLogin />,
      { wrapper: AuthProvider }
    );

    // When
    const customerElement = await screen.findByText(JSON.stringify(CUSTOMER));

    // Then
    expect(customerElement).toBeInTheDocument();
  });

  it('Should not set user when login failed when no user returned', async () => {
    // Given
    jest.spyOn(RestApi, 'getByEmail').mockResolvedValue(undefined);

    render(
      <TestLogin />,
      { wrapper: AuthProvider }
    );

    // When
    const customerElement = await screen.findByText('null');

    // Then
    expect(customerElement).toBeInTheDocument();
    expect(window.alert).toHaveBeenCalledTimes(1);
  });

  it('Should not set user when login failed when empty array returned', async () => {
    // Given
    jest.spyOn(RestApi, 'getByEmail').mockResolvedValue([]);

    render(
      <TestLogin />,
      { wrapper: AuthProvider }
    );

    // When
    const customerElement = await screen.findByText('null');

    // Then
    expect(customerElement).toBeInTheDocument();
    expect(window.alert).toHaveBeenCalledTimes(1);
  });

  it('Should not set user when login failed when password mismatch', async () => {
    // Given
    jest.spyOn(RestApi, 'getByEmail').mockResolvedValue(WRONG_PASSWORD);

    render(
      <TestLogin />,
      { wrapper: AuthProvider }
    );

    // When
    const customerElement = await screen.findByText('null');

    // Then
    expect(customerElement).toBeInTheDocument();
    expect(window.alert).toHaveBeenCalledTimes(1);
  });

  it('Should logout correctly', async () => {
    // Given
    jest.spyOn(RestApi, 'getByEmail').mockResolvedValue(CUSTOMER);

    render(
      <TestLogout />,
      { wrapper: AuthProvider }
    );

    // When
    const customerElement = await screen.findByText('null');

    // Then
    expect(customerElement).toBeInTheDocument();
    expect(window.alert).not.toHaveBeenCalled();
  });
})