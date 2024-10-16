import React, { useEffect, useState } from 'react';
import './styles/EventTable.css';
import { useAuth } from './hooks/AuthContext.jsx';

const EventTable = ({ type, fetchRegistrations, registrations }) => {
  const { user } = useAuth();

  const createHeaders = () => {
  console.log(registrations);
      if (!registrations || !registrations.length > 0) {
        return <td></td>;
      }
      return Object.keys(registrations[0][type])
        .map(value => (
          <td key={value}>{value.toUpperCase()}</td>
        ));
  }

  const createBody = () => {
      if (registrations) {
        return registrations.map(registration => <tr key={registration.id}>{
          Object.keys(registration[type]).map((key, index) => (
            <td key={index}>{registration[type][key]}</td>
          ))
        }</tr>);
    }
    return <tr></tr>;
  }

  useEffect(() => {
    fetchRegistrations(user);
  }, []);

  return (
    <div className='table-container'>
      <table id='event-table'>
        <thead>
          <tr>
            {createHeaders()}
          </tr>
        </thead>
        <tbody>
          {createBody()}
        </tbody>
      </table >
    </div>
  )
}

export default EventTable;