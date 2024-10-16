import React from 'react';
import './styles/FormButtons.css';
import { useAuth } from './hooks/AuthContext';
import { deleteById, post, put } from '../rest/eventapi.js';
import { registerForEvent } from '../rest/registerapi.js';
import { useNavigate } from 'react-router-dom';

const EventFormButtons = ({ event, fetchRegistrations }) => {

  const { user } = useAuth();
  const navigate = useNavigate();

  const handleCreate = async () => {
    const newEvent = event;
    delete newEvent.id;
    const response = await post(newEvent, user);
    alert(`Created new event with id ${response.id}`);
    navigate("/events");
  }

  const handleUpdate = async () => {
    const response = await put(event, user);
    alert(`Updated event with id ${response.id}`);
    navigate("/events");
  }

  const handleDelete = async () => {
    await deleteById(event.id, user);
    alert(`Deleted event with id ${event.id}`);
    navigate("/events");
  }

  const handleRegister = async () => {
    await registerForEvent(event, user);
    alert(`Registered for event with id ${event.id}`);
    fetchRegistrations(user);
  }

  return (
    <>
      <button className="btn" id="btn-delete" onClick={handleDelete}>Delete</button>
      <button className="btn" id="btn-save" onClick={handleUpdate}>Update</button>
      <button className="btn" id="btn-create" onClick={handleCreate}>Create</button>
      <button className="btn" id="btn-register" onClick={handleRegister}>Register</button>
    </>
  )
}

export default EventFormButtons;