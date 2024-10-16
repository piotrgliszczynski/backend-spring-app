import React, { createContext, useContext, useState } from 'react';

const EMPTY_CUSTOMER = {
  id: -1,
  name: "",
  email: "",
  password: ""
};

const CustomerContext = createContext(EMPTY_CUSTOMER);

export const CustomerProvider = ({ children }) => {
  const emptyCustomer = EMPTY_CUSTOMER;
  const [customer, setCustomer] = useState(EMPTY_CUSTOMER);

  return (
    <CustomerContext.Provider value={{ customer, emptyCustomer, setCustomer }}>
      {children}
    </CustomerContext.Provider>
  )
}

export const useCustomer = () => useContext(CustomerContext);