import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import '@testing-library/jest-dom';
import FormButtons from './FormButtons';
import * as CustomerContext from './hooks/CustomerContext';
import * as DataProviderContext from './hooks/DataProviderContext';
import * as ReactRouter from 'react-router';

jest.mock('./hooks/CustomerContext');
jest.mock('./hooks/DataProviderContext');

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

describe('Form Buttons', () => {

  const dataContext = {
    fetchCustomers: jest.fn(),
    deleteCustomer: jest.fn(),
    addCustomer: jest.fn(),
    updateCustomer: jest.fn()
  }
  const mockNavigate = jest.fn();
  const mockSetCustomerData = jest.fn();

  beforeEach(() => {
    jest.spyOn(ReactRouter, 'useNavigate').mockImplementation(() => mockNavigate);
    jest.spyOn(DataProviderContext, 'useCustomerData').mockImplementation(() => dataContext);
  });

  afterEach(() => {
    CustomerContext.useCustomer.mockReset();

    DataProviderContext.useCustomerData.mockReset();
    dataContext.fetchCustomers.mockClear();
    dataContext.deleteCustomer.mockClear();
    dataContext.addCustomer.mockClear();
    dataContext.updateCustomer.mockClear();

    mockNavigate.mockClear();
    mockSetCustomerData.mockClear();
  });

  it('Should have all buttons', () => {
    // Given
    const deleteName = 'Delete';
    const saveName = 'Save';
    const cancelName = 'Cancel';

    const contextValues = {
      customer: EMPTY_CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementation(() => contextValues);

    render(
      <FormButtons customerData={EMPTY_CUSTOMER} setCustomerData={mockSetCustomerData} />,
      { wrapper: BrowserRouter }
    );

    // When
    const deleteButton = screen.getByRole('button', { name: deleteName });
    const saveButton = screen.getByRole('button', { name: saveName });
    const cancelButton = screen.getByRole('button', { name: cancelName });

    // Then
    expect(deleteButton).toBeInTheDocument();
    expect(saveButton).toBeInTheDocument();
    expect(cancelButton).toBeInTheDocument();
  });

  it("Should not delete when customer is not selected", () => {
    // Given
    const contextValues = {
      customer: EMPTY_CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER,
      setCustomer: jest.fn()
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementation(() => contextValues);

    render(
      <FormButtons customerData={EMPTY_CUSTOMER} setCustomerData={mockSetCustomerData} />,
      { wrapper: BrowserRouter }
    );
    const deleteName = 'Delete';
    const deleteButton = screen.getByRole('button', { name: deleteName })

    // When
    fireEvent.click(deleteButton);

    // Then
    expect(dataContext.deleteCustomer).not.toHaveBeenCalled();
    expect(contextValues.setCustomer).not.toHaveBeenCalled();
    expect(mockNavigate).not.toHaveBeenCalled();
  });

  it("Should be able to delete data when customer is selected", () => {
    // Given
    const contextValues = {
      customer: CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER,
      setCustomer: jest.fn()
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementation(() => contextValues);

    render(
      <FormButtons customerData={CUSTOMER} setCustomerData={mockSetCustomerData} />,
      { wrapper: BrowserRouter }
    );
    const deleteName = 'Delete';
    const deleteButton = screen.getByRole('button', { name: deleteName })

    // When
    fireEvent.click(deleteButton);

    // Then
    expect(dataContext.deleteCustomer).toHaveBeenCalledTimes(1);
    expect(dataContext.deleteCustomer).toHaveBeenCalledWith(CUSTOMER.id);
    expect(contextValues.setCustomer).toHaveBeenCalledTimes(1);
    expect(contextValues.setCustomer).toHaveBeenCalledWith(EMPTY_CUSTOMER);
    expect(mockNavigate).toHaveBeenCalledTimes(1);
    expect(mockNavigate).toHaveBeenCalledWith("/");
  });

  it("Should be able to add data when customer is not selected", () => {
    // Given
    const contextValues = {
      customer: EMPTY_CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER,
      setCustomer: jest.fn()
    }
    const typedCustomer = {
      id: EMPTY_CUSTOMER.id,
      name: "Test",
      email: "test@test.com",
      password: "test"
    };
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementation(() => contextValues);

    render(
      <FormButtons customerData={typedCustomer} setCustomerData={mockSetCustomerData} />,
      { wrapper: BrowserRouter }
    );
    const saveName = 'Save';
    const saveButton = screen.getByRole('button', { name: saveName });

    // When
    fireEvent.click(saveButton);

    // Then
    expect(dataContext.addCustomer).toHaveBeenCalledTimes(1);
    expect(mockSetCustomerData).toHaveBeenCalledTimes(1);
    expect(mockSetCustomerData).toHaveBeenCalledWith(contextValues.emptyCustomer);
    expect(mockNavigate).toHaveBeenCalledTimes(1);
    expect(mockNavigate).toHaveBeenCalledWith("/");
  });

  it("Should do nothing when fields are not valid", () => {
    // Given
    const contextValues = {
      customer: EMPTY_CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER,
      setCustomer: jest.fn()
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementation(() => contextValues);

    render(
      <FormButtons customerData={EMPTY_CUSTOMER} setCustomerData={mockSetCustomerData} />,
      { wrapper: BrowserRouter }
    );
    const saveName = 'Save';
    const saveButton = screen.getByRole('button', { name: saveName });

    // When
    fireEvent.click(saveButton);


    // Then
    expect(dataContext.addCustomer).not.toHaveBeenCalled();
  });

  it("Should update when customer is selected", async () => {
    // Given
    const contextValues = {
      customer: CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER,
      setCustomer: jest.fn()
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementation(() => contextValues);

    render(
      <FormButtons customerData={CUSTOMER} setCustomerData={mockSetCustomerData} />,
      { wrapper: BrowserRouter }
    );
    const saveName = 'Save';
    const saveButton = screen.getByRole('button', { name: saveName });

    // When
    await fireEvent.click(saveButton);

    // Then
    expect(dataContext.addCustomer).not.toHaveBeenCalled();
    expect(dataContext.updateCustomer).toHaveBeenCalledTimes(1);
    expect(contextValues.setCustomer).toHaveBeenCalledTimes(1);
    expect(contextValues.setCustomer).toHaveBeenCalledWith(contextValues.emptyCustomer);
    expect(mockNavigate).toHaveBeenCalledTimes(1);
    expect(mockNavigate).toHaveBeenCalledWith("/");
  });

  it("Should navigate to main page when Cancel is clicked and no customer selected", () => {
    // Given
    const contextValues = {
      customer: EMPTY_CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER,
      setCustomer: jest.fn()
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementation(() => contextValues);

    render(
      <FormButtons customerData={EMPTY_CUSTOMER} setCustomerData={mockSetCustomerData} />,
      { wrapper: BrowserRouter }
    );
    const cancelName = 'Cancel';
    const cancelButton = screen.getByRole('button', { name: cancelName });

    // When
    fireEvent.click(cancelButton);

    // Then
    expect(mockNavigate).toHaveBeenCalledTimes(1);
    expect(mockNavigate).toHaveBeenCalledWith("/");
  });

  it("Should deselect customer when customer is selected and cancel clicked", () => {
    // Given
    const contextValues = {
      customer: CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER,
      setCustomer: jest.fn()
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementation(() => contextValues);

    render(
      <FormButtons customerData={CUSTOMER} setCustomerData={mockSetCustomerData} />,
      { wrapper: BrowserRouter }
    );
    const cancelName = 'Cancel';
    const cancelButton = screen.getByRole('button', { name: cancelName });

    // When
    fireEvent.click(cancelButton);

    // Then
    expect(contextValues.setCustomer).toHaveBeenCalledTimes(1);
    expect(contextValues.setCustomer).toHaveBeenCalledWith(contextValues.emptyCustomer);
    expect(mockNavigate).toHaveBeenCalledTimes(1);
    expect(mockNavigate).toHaveBeenCalledWith("/");
  });
});