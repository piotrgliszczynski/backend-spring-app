import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import { fireEvent, render, screen } from '@testing-library/react';
import CustomerTable from './CustomerTable';
import '@testing-library/jest-dom';
import * as CustomerContext from './hooks/CustomerContext';
import * as DataProviderContext from './hooks/DataProviderContext';

jest.mock('./hooks/DataProviderContext');
jest.mock('./hooks/CustomerContext');

const returnData = [
  {
    "id": 0,
    "name": "Mary Jackson",
    "email": "maryj@abc.com",
    "password": "maryj"
  }];

const EMPTY_CUSTOMER = {
  id: -1,
  name: "",
  email: "",
  password: ""
};

describe('Customer Table', () => {

  const dataContext = {
    customerData: returnData,
    fetchCustomers: jest.fn()
  }

  beforeEach(() => {
    jest.spyOn(DataProviderContext, 'useCustomerData').mockImplementation(() => dataContext);
  });

  afterEach(() => {
    CustomerContext.useCustomer.mockClear();

    DataProviderContext.useCustomerData.mockReset();
    dataContext.fetchCustomers.mockClear();
  });

  it('Should contain Name, Email and Pass column row', async () => {
    // Given
    const nameColumn = 'Name';
    const emailColumn = 'Email';
    const passwordColumn = 'Pass';
    const contextValues = {
      customer: EMPTY_CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER,
      setCustomer: jest.fn()
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementationOnce(() => contextValues);

    const { findByRole } = render(
      <CustomerTable />,
      { wrapper: BrowserRouter }
    );

    // When
    const nameElement = await findByRole('columnheader', { name: nameColumn });
    const emailElement = await findByRole('columnheader', { name: emailColumn });
    const passwordElement = await findByRole('columnheader', { name: passwordColumn });

    // Then
    expect(nameElement).toBeInTheDocument();
    expect(emailElement).toBeInTheDocument();
    expect(passwordElement).toBeInTheDocument();
  })

  it('Should contain returned data', async () => {
    // Given
    const contextValues = {
      customer: EMPTY_CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER,
      setCustomer: jest.fn()
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementationOnce(() => contextValues);

    const { findByRole } = render(
      <CustomerTable />,
      { wrapper: BrowserRouter }
    );

    // When
    const name = await findByRole('cell', { name: returnData[0].name });
    const email = await findByRole('cell', { name: returnData[0].email });
    const password = await findByRole('cell', { name: returnData[0].password });

    // Then
    expect(name).toBeInTheDocument();
    expect(email).toBeInTheDocument();
    expect(password).toBeInTheDocument();
    expect(dataContext.fetchCustomers).toHaveBeenCalledTimes(1);
  })

  it('Should fire event and select when clicking on table', async () => {
    // Given
    const contextValues = {
      customer: EMPTY_CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER,
      setCustomer: jest.fn()
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementationOnce(() => contextValues);

    const { findByRole } = render(
      <CustomerTable />,
      { wrapper: BrowserRouter }
    );
    const name = await findByRole('cell', { name: returnData[0].name });
    const customerRow = name.closest('tr');

    // When
    fireEvent.click(customerRow);

    // Then
    expect(contextValues.setCustomer).toHaveBeenCalledTimes(1);
    expect(contextValues.setCustomer).toHaveBeenCalledWith(returnData[0]);
  });

  it('Should have bold row when customer selected', () => {
    // Given
    const contextValues = {
      customer: returnData[0],
      emptyCustomer: EMPTY_CUSTOMER,
      setCustomer: jest.fn()
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementationOnce(() => contextValues);

    const { getByRole } = render(
      <CustomerTable />,
      { wrapper: BrowserRouter }
    );

    // @hen
    const name = getByRole('cell', { name: returnData[0].name });
    const customerRow = name.closest('tr');

    // Then
    expect(customerRow.classList.contains('selected')).toBe(true);
  });

  it('Should deselect a customer when clicked twice', async () => {
    // Given
    let contextValues = {
      customer: EMPTY_CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER,
      setCustomer: jest.fn()
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementation(() => contextValues);

    const { findByRole } = render(
      <CustomerTable />,
      { wrapper: BrowserRouter }
    );
    const name = await findByRole('cell', { name: returnData[0].name });
    const customerRow = name.closest('tr');

    // When
    fireEvent.click(customerRow);
    fireEvent.click(customerRow);

    // Then
    expect(contextValues.setCustomer).toHaveBeenCalledTimes(2);
    expect(contextValues.setCustomer).toHaveBeenCalledWith(returnData[0]);
  })

  it('Should deselect a customer when customer was already selected', async () => {
    // Given
    let contextValues = {
      customer: returnData[0],
      emptyCustomer: EMPTY_CUSTOMER,
      setCustomer: jest.fn()
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementation(() => contextValues);

    const { findByRole } = render(
      <CustomerTable />,
      { wrapper: BrowserRouter }
    );
    const name = await findByRole('cell', { name: returnData[0].name });
    const customerRow = name.closest('tr');

    // When
    fireEvent.click(customerRow);

    // Then
    expect(contextValues.setCustomer).toHaveBeenCalledTimes(1);
    expect(contextValues.setCustomer).toHaveBeenCalledWith(EMPTY_CUSTOMER);
  })
})