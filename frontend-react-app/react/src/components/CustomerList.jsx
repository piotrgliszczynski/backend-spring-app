import React from 'react';
import CustomerListTitle from './CustomerListTitle';
import CustomerTable from './CustomerTable';
import SearchBar from './SearchBar';
import './styles/CustomerList.css';

const CustomerList = () => {
  return (
    <div className="customer-list">
      <div className="list-heading">
        <CustomerListTitle />
        <SearchBar />
      </div>
      <CustomerTable />
    </div>
  )
};

export default CustomerList;