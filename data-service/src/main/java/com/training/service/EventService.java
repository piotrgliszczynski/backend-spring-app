package com.training.service;

import com.training.domain.Event;
import com.training.exception.ElementNotFoundException;
import com.training.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

  private final EventRepository repository;

  public Event createEvent(Event event) {
    return repository.save(event);
  }

  public Event updateEvent(Event event) {
    return repository.save(event);
  }

  public void deleteById(int id) {
    repository.deleteById(id);
  }

  public Event getById(int eventId) throws ElementNotFoundException {
    return repository.findById(eventId)
        .orElseThrow(() -> new ElementNotFoundException("Event with id " + eventId + " not found"));
  }

  public List<Event> getAllEvents() {
    return repository.findAll();
  }

}
