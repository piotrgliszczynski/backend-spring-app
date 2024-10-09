package com.training.service;

import com.training.domain.Event;
import com.training.exception.ElementNotFoundException;
import com.training.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class EventServiceTest {

  private Event event;

  @Autowired
  private EventService service;
  @MockBean
  private EventRepository repository;

  @BeforeEach
  void setupEvent() {
    event = new Event("test", LocalDateTime.now());
  }

  @Test
  void getAllEvents() {
    // Given
    Event event1 = new Event("test2", LocalDateTime.now());
    List<Event> events = List.of(event, event1);
    when(repository.findAll()).thenReturn(events);

    // When
    List<Event> foundEvents = service.getAllEvents();

    // Then
    assertEquals(events.size(), foundEvents.size());
  }

  @Test
  void createEvent() {
    // Given
    when(repository.save(event)).thenReturn(event);

    // When
    Event savedEvent = service.createEvent(event);

    // Then
    assertEquals(event.getName(), savedEvent.getName());
  }

  @Test
  void updateEvent() {
    // Given
    when(repository.save(event)).thenReturn(event);

    // When
    Event updatedEvent = service.updateEvent(event);

    // Then
    assertEquals(event.getName(), updatedEvent.getName());
  }

  @Test
  void deleteById() {
    // Given

    // When
    service.deleteById(1);

    // Then
    verify(repository, times(1)).deleteById(1);
  }

  @Test
  void getById() throws ElementNotFoundException {
    // Given
    when(repository.findById(event.getId())).thenReturn(Optional.of(event));

    // When
    Event foundEvent = service.getById(event.getId());

    // Then
    assertEquals(event.getName(), foundEvent.getName());
  }

  @Test
  void getById_ThrowsWhenEmpty() {
    // Given
    when(repository.findById(event.getId())).thenReturn(Optional.empty());

    // When
    Executable executable = () -> service.getById(event.getId());

    // Then
    assertThrows(ElementNotFoundException.class, executable);
  }
}