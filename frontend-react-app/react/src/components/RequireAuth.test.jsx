import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import { RouterProvider, createMemoryRouter } from 'react-router-dom';
import RequireAuth from './RequireAuth';
import AddUpdateForm from './AddUpdateForm';
import * as CustomerContext from './hooks/CustomerContext';
import * as AuthContext from './hooks/AuthContext';
import LoginForm from './LoginForm';
import { enableFetchMocks } from 'jest-fetch-mock'

enableFetchMocks();

jest.mock('./hooks/CustomerContext');
jest.mock('./hooks/AuthContext');

const EMPTY_CUSTOMER = {
  id: -1,
  name: "",
  email: "",
  password: ""
};

const CUSTOMER = {
  "id": 0,
  "name": "Mary Jackson",
  "email": "maryj@abc.com",
  "password": "maryj"
};


describe('Require Auth', () => {

  let router;

  beforeEach(() => {
    const contextValues = {
      customer: EMPTY_CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementation(() => contextValues);
    fetch.mockImplementation(() => { });

    const routes = [
      {
        path: "/customer-form",
        element: (
          <RequireAuth>
            <AddUpdateForm />
          </RequireAuth>
        )
      },
      {
        path: "/login",
        element: (
          <LoginForm />
        )
      }
    ];
    router = createMemoryRouter(routes, {
      initialEntries: ["/", "/customer-form"],
      initialIndex: 1
    })
  })

  afterEach(() => {
    CustomerContext.useCustomer.mockReset();
    AuthContext.useAuth.mockReset();
    fetch.mockReset();
  });

  it('Should reroute when user is not logged in', async () => {
    // Given    
    const userContext = {
      user: null,
      login: jest.fn()
    };
    jest.spyOn(AuthContext, 'useAuth').mockImplementation(() => userContext);

    render(
      <RouterProvider router={router} />
    );

    // When
    const loginPageTitle = await screen.findByRole('heading', { name: 'Login' });

    // Then
    expect(loginPageTitle).toBeInTheDocument();
  });

  it('Should allow when user is logged in', async () => {
    // Given    
    const userContext = {
      user: CUSTOMER,
      login: jest.fn()
    };
    jest.spyOn(AuthContext, 'useAuth').mockImplementationOnce(() => userContext);

    render(
      <RouterProvider router={router} />
    );

    // When
    const loginPageTitle = await screen.findByRole('heading', { name: /customer/i });

    // Then
    expect(loginPageTitle).toBeInTheDocument();
  });
});