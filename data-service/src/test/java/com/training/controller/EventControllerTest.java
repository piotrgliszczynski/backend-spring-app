package com.training.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.training.domain.Event;
import com.training.domain.dto.EventDto;
import com.training.mapper.EventMapper;
import com.training.mapper.LocalDateTimeTypeAdapter;
import com.training.service.EventService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
class EventControllerTest {

  private final static String URL = "/api/events";
  private final static Gson GSON = new GsonBuilder()
      .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
      .create();
  private MockMvc mockMvc;
  @Autowired
  private WebApplicationContext context;
  @MockBean
  private EventService service;
  @MockBean
  private EventMapper mapper;

  @BeforeEach
  void securitySetup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(springSecurity()).
        build();
  }

  @Test
  @WithMockUser(username = "test@test.com", password = "test")
  void getEvents() throws Exception {
    // Given
    Event event1 = new Event(1, "test1", LocalDateTime.now());
    Event event2 = new Event(2, "test2", LocalDateTime.now());
    List<Event> events = List.of(event1, event2);

    EventDto eventDto1 = new EventDto(1, "test1", LocalDateTime.now());
    EventDto eventDto2 = new EventDto(2, "test2", LocalDateTime.now());
    List<EventDto> eventDtos = List.of(eventDto1, eventDto2);

    when(service.getAllEvents()).thenReturn(events);
    when(mapper.mapToDtoList(events)).thenReturn(eventDtos);

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders
            .get(URL))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(events.size())));
  }

  @Test
  @WithMockUser(username = "test@test.com", password = "test")
  void createEvent() throws Exception {
    // Given
    Event event = new Event(1, "test1", LocalDateTime.now());
    EventDto eventDto = new EventDto(1, "test1", LocalDateTime.now());

    when(mapper.map(eventDto)).thenReturn(event);
    when(service.createEvent(event)).thenReturn(event);
    when(mapper.mapToDto(any())).thenReturn(eventDto);

    String content = GSON.toJson(eventDto);

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders
            .post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8)
            .content(content))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(event.getName())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(event.getId())));
  }

  @Test
  @WithMockUser(username = "test@test.com", password = "test")
  void updateEvent() throws Exception {
    // Given
    Event event = new Event(1, "test1", LocalDateTime.now());
    EventDto eventDto = new EventDto(1, "test1", LocalDateTime.now());

    when(mapper.map(eventDto)).thenReturn(event);
    when(service.createEvent(event)).thenReturn(event);
    when(mapper.mapToDto(any())).thenReturn(eventDto);

    String content = GSON.toJson(eventDto);

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders
            .put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8)
            .content(content))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(event.getName())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(event.getId())));
  }

  @Test
  @WithMockUser(username = "test@test.com", password = "test")
  void deleteById() throws Exception {
    // Given
    int id = 1;

    // When
    mockMvc.perform(MockMvcRequestBuilders
            .delete(URL + "/{id}", id))
        .andExpect(MockMvcResultMatchers.status().isOk());
    verify(service, times(1)).deleteById(id);
  }
}