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
}