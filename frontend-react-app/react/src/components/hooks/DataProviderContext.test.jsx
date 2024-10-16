import React, { useEffect, useState } from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import { DataProvider, useCustomerData } from './DataProviderContext';
import * as RestApi from '../../rest/restdb';

jest.mock('../../rest/restdb');

const returnData = [
  {
    "id": 0,
    "name": "Mary Jackson",
    "email": "maryj@abc.com",
    "password": "maryj"
  },
  {
    "id": 1,
    "name": "Karen Addams",
    "email": "karena@abc.com",
    "password": "karena"
  }
];

const TestFetchCustomers = () => {
  const { customerData, fetchCustomers } = useCustomerData();

  useEffect(() => {
    fetchCustomers();
  }, []);

  return (
    <div>
      {JSON.stringify(customerData)}
    </div>
  )
}

const TestDeleteCustomer = () => {
  const { customerData, deleteCustomer } = useCustomerData();

  useEffect(() => {
    deleteCustomer(returnData[0].id);
  }, []);

  return (
    <div>
      {JSON.stringify(customerData)}
    </div>
  )
}

const TestUpdateCustomer = () => {
  const { customerData, updateCustomer } = useCustomerData();

  useEffect(() => {
    updateCustomer(returnData[0]);
  }, []);

  return (
    <div>
      {JSON.stringify(customerData)}
    </div>
  )
}

const TestCreateCustomer = () => {
  const { customerData, addCustomer } = useCustomerData();

  useEffect(() => {
    addCustomer(returnData[0]);
  }, []);

  return (
    <div>
      {JSON.stringify(customerData)}
    </div>
  )
}

const TestResetSearch = () => {
  const { customerData, fetchCustomers } = useCustomerData();

  useEffect(() => {
    fetchCustomers();
  }, []);

  return (
    <div>
      {JSON.stringify(customerData)}
    </div>
  )
}

const TestSearchCustomer = () => {
  const { customerData, fetchCustomers } = useCustomerData();

  useEffect(() => {
    fetchCustomers("Mary");
  }, []);

  return (
    <div>
      {JSON.stringify(customerData)}
    </div>
  )
}

describe("Data Provider Context", () => {

  beforeEach(() => {
    jest.spyOn(window, 'alert').mockImplementation(() => { });

    jest.spyOn(RestApi, 'getAll').mockResolvedValue(returnData)
    jest.spyOn(RestApi, 'deleteById').mockImplementation(() => { });
    jest.spyOn(RestApi, 'put').mockReturnValue(returnData[0]);
    jest.spyOn(RestApi, 'post').mockReturnValue(returnData[0]);
  });

  afterEach(() => {
    window.alert.mockReset();

    RestApi.getAll.mockReset();
    RestApi.deleteById.mockReset();
    RestApi.put.mockReset();
    RestApi.post.mockReset();
  })

  it("Should set fetched customers", async () => {
    // Given
    render(
      <DataProvider>
        <TestFetchCustomers />
      </DataProvider>
    );

    // When
    const customerElement = await screen.findByText(JSON.stringify(returnData));

    // Then
    expect(customerElement).toBeInTheDocument();
  });

  it("Should reset search when empty search string", async () => {
    // Given
    render(
      <DataProvider>
        <TestResetSearch />
      </DataProvider>
    );

    // When
    const customerElement = await screen.findByText(JSON.stringify(returnData));

    // Then
    expect(customerElement).toBeInTheDocument();
    expect(RestApi.getAll).toHaveBeenCalledTimes(1);
    expect(RestApi.getAll).toHaveBeenCalledWith(undefined);
  });

  it("Should search for customer", async () => {
    // Given
    render(
      <DataProvider>
        <TestSearchCustomer />
      </DataProvider>
    );

    // When
    const customerElement = await screen.findByText(JSON.stringify(returnData));

    // Then
    expect(customerElement).toBeInTheDocument();
    expect(RestApi.getAll).toHaveBeenCalledTimes(1);
    expect(RestApi.getAll).toHaveBeenCalledWith("Mary");
  });

  it("Should delete customer", async () => {
    // Given
    render(
      <DataProvider>
        <TestDeleteCustomer />
      </DataProvider>
    );

    // When
    const customerElement = await screen.findByText(JSON.stringify(returnData));

    // Then
    expect(RestApi.deleteById).toHaveBeenCalledTimes(1);
    expect(RestApi.deleteById).toHaveBeenCalledWith(returnData[0].id);
    expect(window.alert).toHaveBeenCalledTimes(1);
    expect(window.alert.mock.calls[0][0]).toContain(returnData[0].id.toString());
    expect(customerElement).toBeInTheDocument();
  });

  it("Should update customer", async () => {
    // Given
    render(
      <DataProvider>
        <TestUpdateCustomer />
      </DataProvider>
    );

    // When
    const customerElement = await screen.findByText(JSON.stringify(returnData));

    // Then
    expect(RestApi.put).toHaveBeenCalledTimes(1);
    expect(RestApi.put).toHaveBeenCalledWith(returnData[0]);
    expect(window.alert).toHaveBeenCalledTimes(1);
    expect(window.alert.mock.calls[0][0]).toContain(returnData[0].id.toString());
    expect(customerElement).toBeInTheDocument();
  });

  it("Should create customer", async () => {
    // Given
    render(
      <DataProvider>
        <TestCreateCustomer />
      </DataProvider>
    );

    // When
    const customerElement = await screen.findByText(JSON.stringify(returnData));

    // Then
    expect(RestApi.post).toHaveBeenCalledTimes(1);
    expect(RestApi.post).toHaveBeenCalledWith(returnData[0]);
    expect(window.alert).toHaveBeenCalledTimes(1);
    expect(window.alert.mock.calls[0][0]).toContain(returnData[0].id.toString());
    expect(customerElement).toBeInTheDocument();
  });
})