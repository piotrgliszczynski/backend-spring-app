import React from "react";
import { useCustomer } from "./hooks/CustomerContext";
import './styles/AddUpdateFormTitle.css';

const AddUpdateFormTitle = (props) => {

  const { emptyCustomer } = useCustomer();

  const setTitle = () => {
    return props.customerId !== emptyCustomer.id ? 'Update' : 'Add'
  }

  return (
    <h2 id="add-update-form-title">{setTitle()} customer</h2>
  )
}

export default AddUpdateFormTitle