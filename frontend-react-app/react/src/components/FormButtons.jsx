import React from 'react';
import { useNavigate } from "react-router";
import { useCustomer } from "./hooks/CustomerContext";
import { useCustomerData } from "./hooks/DataProviderContext";
import { isValidText, isValidEmail } from "../utils/Validator";
import './styles/FormButtons.css';

const FormButtons = (props) => {

  const { customerData, setCustomerData } = props;
  const { emptyCustomer, setCustomer } = useCustomer();
  const { deleteCustomer, addCustomer, updateCustomer } = useCustomerData();
  const navigate = useNavigate();

  const isFormValid = () => {
    return isValidText(customerData.name)
      && isValidEmail(customerData.email)
      && isValidText(customerData.password);
  }

  const onDelete = () => {
    if (customerData.id !== emptyCustomer.id) {
      deleteCustomer(customerData.id);
      setCustomer(emptyCustomer);
      navigate("/");
    }
  }

  const onSave = async () => {
    if (!isFormValid()) {
      return;
    }

    if (customerData.id === emptyCustomer.id) {
      let newCustomer = customerData;
      delete newCustomer.id;

      addCustomer(newCustomer);
      setCustomerData(emptyCustomer);
      navigate("/");
      return;
    }

    await updateCustomer(customerData);
    setCustomer(emptyCustomer);
    navigate("/");
  }

  const onCancel = () => {
    if (customerData.id !== emptyCustomer.id) {
      setCustomer(emptyCustomer);
    }
    navigate("/");
  }

  return (
    <>
      <button className="btn" id="btn-delete" onClick={onDelete}>Delete</button>
      <button className="btn" id="btn-save" onClick={onSave}>Save</button>
      <button className="btn" id="btn-cancel" onClick={onCancel}>Cancel</button>
    </>
  )
}

export default FormButtons;