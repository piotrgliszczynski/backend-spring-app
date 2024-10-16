import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import { render, screen } from '@testing-library/react';
import AddUpdateFormTitle from './AddUpdateFormTitle';
import '@testing-library/jest-dom';
import * as CustomerContext from './hooks/CustomerContext';

jest.mock('./hooks/CustomerContext');

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

describe('Add Update Form Title', () => {
  afterEach(() => {
    CustomerContext.useCustomer.mockReset();
  });

  it('Should have correct title in Add state', () => {
    // Given
    const title = 'Add customer';

    const contextValues = {
      emptyCustomer: EMPTY_CUSTOMER
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementation(() => contextValues);

    render(
      <AddUpdateFormTitle customerId={EMPTY_CUSTOMER.id} />,
      { wrapper: BrowserRouter }
    );

    // When
    const titleElement = screen.getByRole('heading', { name: title });

    // Then
    expect(titleElement).toBeInTheDocument();
  });



  it('Should have correct title in Update state', () => {
    // Given
    const title = 'Update customer';

    const contextValues = {
      emptyCustomer: EMPTY_CUSTOMER
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementation(() => contextValues);

    render(
      <AddUpdateFormTitle customerId={CUSTOMER.id} />,
      { wrapper: BrowserRouter }
    );

    // When
    const titleElement = screen.getByRole('heading', { name: title });

    // Then
    expect(titleElement).toBeInTheDocument();
  });
});
