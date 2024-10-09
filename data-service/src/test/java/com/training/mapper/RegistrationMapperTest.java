package com.training.mapper;

import com.training.domain.Customer;
import com.training.domain.Event;
import com.training.domain.Registration;
import com.training.domain.dto.EventDto;
import com.training.domain.dto.RegistrationCustomerDto;
import com.training.domain.dto.RegistrationDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class RegistrationMapperTest {

  @Autowired
  private RegistrationMapper mapper;
  @MockBean
  private CustomerMapper customerMapper;
  @MockBean
  private EventMapper eventMapper;

  @Test
  void mapToDto() {
    // Given
    Customer customer = new Customer(1, "test@test.com", "test", "test");
    Event event = new Event(1, "test", LocalDateTime.now());
    Registration registration = new Registration(1, customer, event);

    RegistrationCustomerDto customerDto = new RegistrationCustomerDto(
        customer.getId(),
        customer.getName(),
        customer.getEmail());
    when(customerMapper.mapToRegistrationDto(customer)).thenReturn(customerDto);

    EventDto eventDto = new EventDto(event.getId(), event.getName(), event.getDate());
    when(eventMapper.mapToDto(event)).thenReturn(eventDto);

    // When
    RegistrationDto registrationDto = mapper.mapToDto(registration);

    // Then
    assertAll(
        () -> assertEquals(registration.getId(), registrationDto.getId()),
        () -> assertEquals(registration.getCustomer().getId(),
            registrationDto.getCustomer().getId()),
        () -> assertEquals(registration.getCustomer().getName(),
            registrationDto.getCustomer().getName()),
        () -> assertEquals(registration.getCustomer().getEmail(),
            registrationDto.getCustomer().getEmail()),
        () -> assertEquals(registration.getEvent().getId(),
            registrationDto.getEvent().getId()),
        () -> assertEquals(registration.getEvent().getName(),
            registrationDto.getEvent().getName())
    );
  }

  @Test
  void mapToDtoList() {
    // Given
    Customer customer1 = new Customer(1, "test1@test.com", "test1", "test1");
    Customer customer2 = new Customer(2, "test2@test.com", "test2", "test2");
    RegistrationCustomerDto customerDto1 =
        new RegistrationCustomerDto(1, "test1", "test1@test.com");
    RegistrationCustomerDto customerDto2 =
        new RegistrationCustomerDto(2, "test2", "test2@test.com");
    when(customerMapper.mapToRegistrationDto(customer1)).thenReturn(customerDto1);
    when(customerMapper.mapToRegistrationDto(customer2)).thenReturn(customerDto2);

    Event event1 = new Event(1, "test1", LocalDateTime.now());
    Event event2 = new Event(2, "test2", LocalDateTime.now());
    EventDto eventDto1 = new EventDto(1, "test1", LocalDateTime.now());
    EventDto eventDto2 = new EventDto(2, "test2", LocalDateTime.now());
    when(eventMapper.mapToDto(event1)).thenReturn(eventDto1);
    when(eventMapper.mapToDto(event2)).thenReturn(eventDto2);

    List<Registration> registrations = List.of(
        new Registration(customer1, event1),
        new Registration(customer2, event2)
    );

    // When
    List<RegistrationDto> registrationDtos = mapper.mapToDtoList(registrations);

    // Then
    assertEquals(registrations.size(), registrationDtos.size());
  }
}