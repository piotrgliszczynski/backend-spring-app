import React from "react";
import './styles/SearchBar.css';
import { useCustomerData } from "./hooks/DataProviderContext";

const SearchBar = () => {

  const { fetchCustomers } = useCustomerData();

  const onType = (event) => {
    fetchCustomers(event.target.value);
  }

  return (
    <div className="search-bar-container">
      <input type="text" name="search-bar" id="search-bar"
        placeholder="Search for customer name" onChange={onType} />
    </div>
  )
}

export default SearchBar;