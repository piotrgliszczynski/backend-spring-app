import React from 'react';
import './styles/EventList.css';
import EventListTitle from './EventListTitle';
import EventTable from './EventTable';

const EventList = () => {
  return (
    <div className="event-list">
      <div className="list-heading">
        <EventListTitle />
      </div>
      <EventTable />
    </div>
  )
};

export default EventList;