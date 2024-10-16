import React from 'react';
import { fireEvent, render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import SearchBar from './SearchBar';
import { BrowserRouter } from 'react-router-dom';
import * as DataProviderContext from './hooks/DataProviderContext';

jest.mock('./hooks/DataProviderContext');

describe("Search Bar", () => {

  const dataContext = {
    fetchCustomers: jest.fn()
  }

  beforeEach(() => {
    jest.spyOn(DataProviderContext, 'useCustomerData').mockImplementation(() => dataContext);
  });

  afterEach(() => {
    DataProviderContext.useCustomerData.mockReset();
    dataContext.fetchCustomers.mockClear();
  });


  it("Should contain search bar", () => {
    // Given
    const placeholderSearch = 'Search for customer name';

    render(
      <SearchBar />,
      { wrapper: BrowserRouter }
    );

    // When
    const inputElement = screen.getByPlaceholderText(placeholderSearch);

    // Then
    expect(inputElement).toBeInTheDocument();
  });

  it("Should be able to enter and search", () => {
    // Given
    const placeholderSearch = 'Search for customer name';

    render(
      <SearchBar />,
      { wrapper: BrowserRouter }
    );
    const inputElement = screen.getByPlaceholderText(placeholderSearch);

    // When
    fireEvent.change(inputElement, { target: { value: "test" } });

    // Then
    expect(dataContext.fetchCustomers).toHaveBeenCalledTimes(1);
    expect(dataContext.fetchCustomers).toHaveBeenCalledWith('test');
  })
})