package com.training.mapper;

import com.training.domain.Event;
import com.training.domain.dto.EventDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventMapper {

  public List<EventDto> mapToDtoList(List<Event> events) {
    return events.stream()
        .map(this::mapToDto)
        .toList();
  }

  public EventDto mapToDto(Event event) {
    return new EventDto(
        event.getId(),
        event.getName(),
        event.getDate()
    );
  }

  public Event map(EventDto eventDto) {
    return new Event(
        eventDto.getId(),
        eventDto.getName(),
        eventDto.getDate()
    );
  }

}
