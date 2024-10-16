import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import AddUpdateForm from './AddUpdateForm';
import '@testing-library/jest-dom';
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

describe('Add-Update form', () => {

  const dataContext = {
    fetchCustomers: jest.fn(),
    deleteCustomer: jest.fn(),
    addCustomer: jest.fn(),
    updateCustomer: jest.fn()
  }
  const mockNavigate = jest.fn();

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
  });

  it('Should have all components and in state Add', () => {
    // Given
    const title = 'Add customer';
    const nameLabel = 'Name:';
    const namePlaceholder = 'Customer Name';

    const emailLabel = 'Email:';
    const emailPlaceholder = 'name@company.com';

    const passwordLabel = 'Pass:';
    const passwordPlaceholder = 'password';

    const deleteName = 'Delete';
    const saveName = 'Save';
    const cancelName = 'Cancel';

    const contextValues = {
      customer: EMPTY_CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER
    }

    jest.spyOn(CustomerContext, 'useCustomer').mockImplementation(() => contextValues);

    render(
      <AddUpdateForm />,
      { wrapper: BrowserRouter }
    );

    // When
    const titleElement = screen.getByRole('heading', { name: title });
    const nameLabelElement = screen.getByText(nameLabel);
    const nameInputElement = screen.getByPlaceholderText(namePlaceholder);
    const emailLabelElement = screen.getByText(emailLabel);
    const emailPlaceholderElement = screen.getByPlaceholderText(emailPlaceholder);
    const passwordLabelElement = screen.getByText(passwordLabel);
    const passwordPlaceholderElement = screen.getByPlaceholderText(passwordPlaceholder);
    const deleteButton = screen.getByRole('button', { name: deleteName });
    const saveButton = screen.getByRole('button', { name: saveName });
    const cancelButton = screen.getByRole('button', { name: cancelName });

    // Then
    expect(titleElement).toBeInTheDocument();
    expect(nameLabelElement).toBeInTheDocument();
    expect(nameInputElement).toBeInTheDocument();
    expect(emailLabelElement).toBeInTheDocument();
    expect(emailPlaceholderElement).toBeInTheDocument();
    expect(passwordLabelElement).toBeInTheDocument();
    expect(passwordPlaceholderElement).toBeInTheDocument();
    expect(deleteButton).toBeInTheDocument();
    expect(saveButton).toBeInTheDocument();
    expect(cancelButton).toBeInTheDocument();
  });

  it("Should fill form when in Update state", async () => {
    // Given
    const title = 'Update customer';
    const nameValue = CUSTOMER.name;
    const emailValue = CUSTOMER.email;
    const passwordValue = CUSTOMER.password;
    const contextValues = {
      customer: CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER
    }

    jest.spyOn(CustomerContext, 'useCustomer').mockImplementation(() => contextValues);

    render(
      <AddUpdateForm />,
      { wrapper: BrowserRouter }
    );

    // When
    const titleElement = await screen.findByRole('heading', { name: title });
    const nameInput = await screen.findByDisplayValue(nameValue);
    const emailInput = await screen.findByDisplayValue(emailValue);
    const passwordInput = await screen.findByDisplayValue(passwordValue);

    // Then
    expect(titleElement).toBeInTheDocument();
    expect(nameInput).toBeInTheDocument();
    expect(emailInput).toBeInTheDocument();
    expect(passwordInput).toBeInTheDocument();
  });

  it("Should be able to enter data", async () => {
    // Given
    const nameLabel = 'Name:';
    const emailLabel = 'Email:';
    const passwordLabel = 'Pass:';

    const contextValues = {
      customer: EMPTY_CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementation(() => contextValues);

    render(
      <AddUpdateForm />,
      { wrapper: BrowserRouter }
    );

    const nameInput = screen.getByLabelText(nameLabel);
    const emailInput = screen.getByLabelText(emailLabel);
    const passwordInput = screen.getByLabelText(passwordLabel);

    // When
    fireEvent.change(nameInput, { target: { value: 'test' } });
    fireEvent.change(emailInput, { target: { value: 'test@test.com' } });
    fireEvent.change(passwordInput, { target: { value: '12345' } });

    const changedName = await screen.findByLabelText(nameLabel);
    const changedEmail = await screen.findByLabelText(emailLabel);
    const changedPassword = await screen.findByLabelText(passwordLabel);

    // Then
    expect(changedName).toBeInTheDocument();
    expect(changedEmail).toBeInTheDocument();
    expect(changedPassword).toBeInTheDocument();
  });

  it("Should deselect customer when Cancel is clicked", async () => {
    // Given
    const namePlaceholder = 'Customer Name';
    const emailPlaceholder = 'name@company.com';
    const passwordPlaceholder = 'password';
    const contextValues = {
      customer: CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER,
      setCustomer: jest.fn()
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementation(() => contextValues);

    render(
      <AddUpdateForm />,
      { wrapper: BrowserRouter }
    );
    const cancelName = 'Cancel';
    const cancelButton = screen.getByRole('button', { name: cancelName });

    // When
    fireEvent.click(cancelButton);
    const emptyName = await screen.findByPlaceholderText(namePlaceholder);
    const emptyEmail = await screen.findByPlaceholderText(emailPlaceholder);
    const emptyPassword = await screen.findByPlaceholderText(passwordPlaceholder);

    // Then
    expect(emptyName).toBeInTheDocument();
    expect(emptyEmail).toBeInTheDocument();
    expect(emptyPassword).toBeInTheDocument();
    expect(mockNavigate).toHaveBeenCalledTimes(1);
    expect(mockNavigate).toHaveBeenCalledWith("/");
  });

  it("Should display errors when field has errors", async () => {
    // Given
    const contextValues = {
      customer: EMPTY_CUSTOMER,
      emptyCustomer: EMPTY_CUSTOMER,
      setCustomer: jest.fn()
    }
    jest.spyOn(CustomerContext, 'useCustomer').mockImplementation(() => contextValues);

    render(
      <BrowserRouter>
        <AddUpdateForm />
      </BrowserRouter>
    );

    // When
    const errorElementName = await screen.findByText('Enter valid name', { exact: false });
    const errorElementEmail = await screen.findByText('Enter valid email', { exact: false });
    const errorElementPassword = await screen.findByText('Enter valid password', { exact: false });

    // Then
    expect(errorElementName).toBeInTheDocument();
    expect(errorElementEmail).toBeInTheDocument();
    expect(errorElementPassword).toBeInTheDocument();
  });
});