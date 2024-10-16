package com.training.controller;

import com.training.domain.Customer;
import com.training.domain.Event;
import com.training.domain.Registration;
import com.training.domain.dto.EventDto;
import com.training.domain.dto.RegistrationCustomerDto;
import com.training.domain.dto.RegistrationDto;
import com.training.mapper.RegistrationMapper;
import com.training.service.CustomerService;
import com.training.service.EventService;
import com.training.service.RegistrationService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
class RegistrationControllerTest {

  private static final String URL = "/api/registrations";

  private MockMvc mockMvc;
  @Autowired
  private WebApplicationContext context;
  @MockBean
  private RegistrationService registrationService;
  @MockBean
  private CustomerService customerService;
  @MockBean
  private EventService eventService;
  @MockBean
  private RegistrationMapper registrationMapper;

  private List<Customer> customers;
  private List<RegistrationCustomerDto> customersDto;
  private List<Event> events;
  private List<EventDto> eventsDto;

  @BeforeEach
  void securitySetup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(springSecurity()).
        build();
  }

  @BeforeEach
  void dataSetup() {
    Customer customer1 = new Customer(1, "test1@test.com", "test1", "test1");
    Customer customer2 = new Customer(1, "test2@test.com", "test2", "test2");
    customers = List.of(customer1, customer2);

    RegistrationCustomerDto customerDto1 =
        new RegistrationCustomerDto(1, "test1", "test1@test.com");
    RegistrationCustomerDto customerDto2 =
        new RegistrationCustomerDto(2, "test2", "test2@test.com");
    customersDto = List.of(customerDto1, customerDto2);

    Event event1 = new Event(1, "test1", LocalDateTime.now());
    Event event2 = new Event(2, "test2", LocalDateTime.now());
    events = List.of(event1, event2);

    EventDto eventDto1 = new EventDto(1, "test1", LocalDateTime.now());
    EventDto eventDto2 = new EventDto(2, "test2", LocalDateTime.now());
    eventsDto = List.of(eventDto1, eventDto2);
  }

  @Test
  @WithMockUser(username = "test1@test.com", password = "test1")
  void getRegistrationsByCustomer() throws Exception {
    // Given
    Customer customer = customers.get(0);
    Registration registration1 = new Registration(1, customer, events.get(0));
    Registration registration2 = new Registration(2, customer, events.get(1));
    List<Registration> registrations = List.of(registration1, registration2);

    RegistrationDto registrationDto1 =
        new RegistrationDto(1, customersDto.get(0), eventsDto.get(0));
    RegistrationDto registrationDto2 =
        new RegistrationDto(2, customersDto.get(0), eventsDto.get(1));
    List<RegistrationDto> registrationDtos = List.of(registrationDto1, registrationDto2);

    when(registrationService.findAllByCustomerId(customer.getId())).thenReturn(registrations);
    when(registrationMapper.mapToDtoList(registrations)).thenReturn(registrationDtos);

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders
            .get(URL + "/customer/" + customer.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
  }

  @Test
  @WithMockUser(username = "test1@test.com", password = "test1")
  void getRegistrationsByEvent() throws Exception {
    // Given
    Event event = events.get(0);
    Registration registration1 = new Registration(1, customers.get(0), event);
    Registration registration2 = new Registration(2, customers.get(0), event);
    List<Registration> registrations = List.of(registration1, registration2);

    RegistrationDto registrationDto1 =
        new RegistrationDto(1, customersDto.get(0), eventsDto.get(0));
    RegistrationDto registrationDto2 =
        new RegistrationDto(2, customersDto.get(1), eventsDto.get(0));
    List<RegistrationDto> registrationDtos = List.of(registrationDto1, registrationDto2);

    when(registrationService.findAllByEventId(event.getId())).thenReturn(registrations);
    when(registrationMapper.mapToDtoList(registrations)).thenReturn(registrationDtos);

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders
            .get(URL + "/event/{eventId}", event.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
  }

  @Test
  @WithMockUser(username = "test1@test.com", password = "test1")
  void createRegistrationForCustomer() throws Exception {
    // Given
    Customer customer = customers.get(0);
    Event event = events.get(0);
    Registration registration = new Registration(1, customer, event);
    RegistrationDto registrationDto = new RegistrationDto(1, customersDto.get(0), eventsDto.get(0));

    when(customerService.getCustomerByEmail(customer.getEmail())).thenReturn(customer);
    when(eventService.getById(event.getId())).thenReturn(event);
    when(registrationService.createRegistration(any())).thenReturn(registration);
    when(registrationMapper.mapToDto(any())).thenReturn(registrationDto);

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders
            .post(URL + "/event/{eventId}", event.getId()))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath(
            "$.customer.name", Matchers.is(customer.getName())))
        .andExpect(MockMvcResultMatchers.jsonPath(
            "$.customer.email", Matchers.is(customer.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath(
            "$.event.name", Matchers.is(event.getName())));
  }

  @Test
  @WithMockUser(username = "test1@test.com", password = "test1")
  void deleteRegistration() throws Exception {
    // Given
    int id = 1;

    // When
    mockMvc.perform(MockMvcRequestBuilders
            .delete(URL + "/{id}", id))
        .andExpect(MockMvcResultMatchers.status().isOk());

    // Then
    verify(registrationService, times(1)).deleteById(id);
  }
}