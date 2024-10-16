import React, { useEffect, useState } from 'react';
import './styles/EventTable.css';
import { getAll } from '../rest/eventapi.js';
import { useAuth } from './hooks/AuthContext.jsx';
import { useNavigate } from 'react-router-dom';

const EventTable = () => {
  const { user } = useAuth();
  const [events, setEvents] = useState([]);
  const navigate = useNavigate();

  const handleClick = (event) => {
    navigate("/event-form", { state: event });
  }

  useEffect(() => {
    getAll(user).then(response => setEvents(response));
  }, []);

  return (
    <div className='table-container'>
      <table id='event-table'>
        <thead>
          <tr>
            <th>Name</th>
            <th>Date</th>
          </tr>
        </thead>
        <tbody>
          {events.map(event => (
            <tr key={event.id} onClick={() => handleClick(event)}>
              <td>{event.name}</td>
              <td>{event.date}</td>
            </tr>
          ))}
        </tbody>
      </table >
    </div>
  )
}

export default EventTable;