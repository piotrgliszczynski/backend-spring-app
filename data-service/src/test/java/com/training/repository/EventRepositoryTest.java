package com.training.repository;

import com.training.domain.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EventRepositoryTest {

  @Autowired
  private EventRepository repository;

  @Test
  void shouldSaveEvent() {
    // Given
    Event event = new Event("test", LocalDateTime.now());

    // When
    repository.save(event);
    int id = event.getId();

    // Then
    Optional<Event> savedEvent = repository.findById(id);
    assertTrue(savedEvent.isPresent());
    assertAll(
        () -> assertEquals(event.getName(), savedEvent.get().getName())
    );

    // Cleanup
    repository.deleteById(id);
  }

  @Test
  void shouldUpdateEvent() {
    // Given
    Event event = new Event("test", LocalDateTime.now());
    repository.save(event);
    int id = event.getId();
    Event updatedEvent = new Event(id, "testChanged", LocalDateTime.now());

    // When
    repository.save(updatedEvent);

    // Then
    Optional<Event> savedEvent = repository.findById(id);
    assertTrue(savedEvent.isPresent());
    assertAll(
        () -> assertEquals(updatedEvent.getName(), savedEvent.get().getName())
    );

    // Cleanup
    repository.deleteById(id);
  }

  @Test
  void shouldGetAllEvents() {
    // Given
    Event event1 = new Event("test1", LocalDateTime.now());
    Event event2 = new Event("test2", LocalDateTime.now());
    repository.saveAll(List.of(event1, event2));
    int id1 = event1.getId();
    int id2 = event2.getId();

    // When
    List<Event> foundEvents = repository.findAll();

    // Then
    assertEquals(2, foundEvents.size());

    // Cleanup
    repository.deleteAllById(List.of(id1, id2));
  }

  @Test
  void shouldDeleteEvent() {
    // Given
    Event event = new Event("test", LocalDateTime.now());
    repository.save(event);
    int id = event.getId();

    // When
    repository.deleteById(id);

    // Then
    Optional<Event> savedEvent = repository.findById(id);
    assertFalse(savedEvent.isPresent());

    // Cleanup
    repository.deleteById(id);
  }

  @Test
  void findById() {
    // Given
    Event event = new Event("test", LocalDateTime.now());
    repository.save(event);
    int id = event.getId();

    // When
    Optional<Event> savedEvent = repository.findById(id);

    // Then
    assertTrue(savedEvent.isPresent());
    assertAll(
        () -> assertEquals(event.getName(), savedEvent.get().getName())
    );

    // Cleanup
    repository.deleteById(id);
  }

  @Test
  void findById_ShouldBeEmptyWhenEventNotSaved() {
    // Given
    Event event = new Event(1, "test", LocalDateTime.now());
    int id = event.getId();

    // When
    Optional<Event> savedEvent = repository.findById(id);

    // Then
    assertFalse(savedEvent.isPresent());

    // Cleanup
    repository.deleteById(id);
  }
}
