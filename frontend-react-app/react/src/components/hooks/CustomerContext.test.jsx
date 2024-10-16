import React, { useEffect } from 'react';
import { render, screen } from '@testing-library/react';
import { CustomerProvider, useCustomer } from './CustomerContext';
import '@testing-library/jest-dom';

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

const TestingInitialCustomer = () => {
  const { customer } = useCustomer();
  return (
    <div>
      {JSON.stringify(customer)}
    </div>
  )
};

const TestingEmptyCustomer = () => {
  const { emptyCustomer } = useCustomer();
  return (
    <div>
      {JSON.stringify(emptyCustomer)}
    </div>
  )
};

const TestingSetCustomer = () => {
  const { customer, setCustomer } = useCustomer();

  useEffect(() => {
    setCustomer(CUSTOMER);
  })

  return (
    <div>
      {JSON.stringify(customer)}
    </div>
  )
};

describe("Customer Context Provider", () => {
  it("Initial selected customer should by empty", () => {
    // Given
    render(
      <CustomerProvider>
        <TestingInitialCustomer />
      </CustomerProvider>
    )

    // When
    const customerElement = screen.getByText(JSON.stringify(EMPTY_CUSTOMER));

    // Then
    expect(customerElement).toBeInTheDocument();
  });

  it("Empty customer should by empty", () => {
    // Given
    render(
      <CustomerProvider>
        <TestingEmptyCustomer />
      </CustomerProvider>
    )

    // When
    const customerElement = screen.getByText(JSON.stringify(EMPTY_CUSTOMER));

    // Then
    expect(customerElement).toBeInTheDocument();
  });

  it("Customer should by set properly", async () => {
    // Given
    render(
      <CustomerProvider>
        <TestingSetCustomer />
      </CustomerProvider>
    )

    // When
    const customerElement = await screen.findByText(JSON.stringify(CUSTOMER));

    // Then
    expect(customerElement).toBeInTheDocument();
  })
});