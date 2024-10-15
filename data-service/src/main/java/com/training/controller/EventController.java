package com.training.controller;

import com.training.domain.Event;
import com.training.domain.dto.EventDto;
import com.training.mapper.EventMapper;
import com.training.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

  private final EventService service;
  private final EventMapper mapper;

  @PostMapping
  public ResponseEntity<EventDto> createEvent(@RequestBody EventDto eventDto) {
    Event event = mapper.map(eventDto);
    Event createdEvent = service.createEvent(event);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(mapper.mapToDto(createdEvent));
  }

  @PutMapping
  public ResponseEntity<EventDto> updateEvent(@RequestBody EventDto eventDto) {
    Event event = mapper.map(eventDto);
    Event updatedEvent = service.updateEvent(event);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(mapper.mapToDto(updatedEvent));
  }

  @DeleteMapping("{eventId}")
  public ResponseEntity<Void> deleteById(@PathVariable int eventId) {
    service.deleteById(eventId);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<List<EventDto>> getEvents() {
    List<Event> events = service.getAllEvents();
    List<EventDto> mappedEvents = mapper.mapToDtoList(events);
    return ResponseEntity.ok(mappedEvents);
  }

}
