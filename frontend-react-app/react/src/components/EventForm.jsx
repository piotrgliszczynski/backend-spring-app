import React, { useState } from "react";
import './styles/AddUpdateForm.css';
import { useLocation } from "react-router-dom";
import EventFormTitle from "./EventFormTitle";
import EventFormButtons from "./EventFormButtons";
import RegistrationsTable from './RegistrationsTable';
import { getAllByEvent } from "../rest/registerapi";

const EventForm = () => {
  const location = useLocation();
  const [event, setEvent] = useState(location.state);
  const [registrations, setRegistrations] = useState([]);

  const onType = (field, event) => {
    setEvent((prevEvent) => {
      return {
        ...prevEvent,
        [field]: event.target.value
      }
    })
  }

  const fetchRegistrations = (user) => {
    getAllByEvent(event, user).then(response => setRegistrations(response));
  }

  return (
    <div className="add-update-form">
      <EventFormTitle />
      <div className="form-parent">
        <label htmlFor="name">Name:</label>
        <input type="text" id="name"
          placeholder="Event Name"
          value={event.name}
          onChange={(event) => onType('name', event)}></input>
        <label htmlFor="date">Email:</label>
        <input type="text" id="date"
          placeholder="Date"
          value={event.date}
          onChange={(event) => onType('date', event)}></input>


        <div className="crud-buttons">
          <EventFormButtons event={event} fetchRegistrations={fetchRegistrations} />
        </div>
      </div>
      <div>
        <RegistrationsTable type='customer' fetchRegistrations={fetchRegistrations} registrations={registrations} />
      </div>
    </div >
  )
}

export default EventForm; 