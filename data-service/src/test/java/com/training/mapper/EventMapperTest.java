package com.training.mapper;

import com.training.domain.Event;
import com.training.domain.dto.EventDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EventMapperTest {

  @Autowired
  private EventMapper mapper;

  private EventDto eventDto;
  private Event event;

  @BeforeEach
  void createData() {
    eventDto = new EventDto(1, "test",
        LocalDateTime.of(2024, 10, 8, 15, 0));
    event = new Event(1, "test",
        LocalDateTime.of(2024, 10, 8, 15, 0));
  }

  @Test
  void mapToDtoList() {
    // Given
    List<Event> events = List.of(
        new Event(1, "test1", LocalDateTime.of(2024, 10, 8, 15, 0)),
        new Event(2, "test2", LocalDateTime.of(2024, 10, 8, 15, 0))
    );

    // When
    List<EventDto> customerDtos = mapper.mapToDtoList(events);

    // Then
    assertAll(
        () -> assertEquals(events.size(), customerDtos.size())
    );
  }

  @Test
  void mapToDto() {
    // Given

    // When
    EventDto actualDto = mapper.mapToDto(event);

    // Then
    assertAll(
        () -> assertEquals(eventDto.getId(), actualDto.getId()),
        () -> assertEquals(eventDto.getName(), actualDto.getName()),
        () -> assertEquals(eventDto.getDate(), actualDto.getDate())
    );
  }

  @Test
  void map() {
    // Given

    // When
    Event actualEvent = mapper.map(eventDto);

    // Then
    assertAll(
        () -> assertEquals(event.getId(), actualEvent.getId()),
        () -> assertEquals(event.getName(), actualEvent.getName()),
        () -> assertEquals(event.getDate(), actualEvent.getDate())
    );
  }
}
