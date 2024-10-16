import React, { createContext, useContext, useState } from 'react';
import { getAll, post, put, deleteById } from '../../rest/customersapi';
import { useAuth } from './AuthContext';

const DataContext = createContext([]);

export const DataProvider = ({ children }) => {
  const [customerData, setCustomerData] = useState([]);
  const { user } = useAuth();


  const fetchCustomers = (searchTerm) => {
    getAll(searchTerm, user).then(response => setCustomerData(response));
  }

  const deleteCustomer = async (id) => {
    await deleteById(id, user);
    alert(`Customer with id ${id} was deleted`);
    fetchCustomers();
  }

  const addCustomer = async (newCustomer) => {
    const customerResponse = await post(newCustomer, user);
    alert(`New customer with id ${customerResponse.id} was created`);
    fetchCustomers();
  }

  const updateCustomer = async (changedCustomer) => {
    const customerResponse = await put(changedCustomer, user);
    alert(`Customer with id ${customerResponse.id} was updated`);
    fetchCustomers();
  }

  return (
    <DataContext.Provider value={
      { customerData, fetchCustomers, deleteCustomer, addCustomer, updateCustomer }
    }>
      {children}
    </DataContext.Provider>
  )
}

export const useCustomerData = () => useContext(DataContext);