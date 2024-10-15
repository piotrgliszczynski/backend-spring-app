package com.training.service;

import com.training.domain.Customer;
import com.training.domain.Event;
import com.training.domain.Registration;
import com.training.exception.DuplicateElementsException;
import com.training.repository.RegistrationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class RegistrationServiceTest {

  private List<Customer> customers;
  private List<Event> events;

  @Autowired
  private RegistrationService service;
  @MockBean
  private RegistrationRepository repository;

  @BeforeEach
  void setupData() {
    Customer customer1 = new Customer(1, "test1@test.com", "test1", "test1");
    Customer customer2 = new Customer(2, "test2@test.com", "test2", "test2");
    customers = List.of(customer1, customer2);

    Event event1 = new Event(1, "test1", LocalDateTime.now());
    Event event2 = new Event(2, "test2", LocalDateTime.now());
    events = List.of(event1, event2);
  }

  @Test
  void findAllByCustomerId() {
    // Given
    List<Registration> registrations = List.of(
        new Registration(customers.get(0), events.get(0)),
        new Registration(customers.get(0), events.get(1))
    );
    int customerId = customers.get(0).getId();
    when(repository.findAllByCustomerId(customerId)).thenReturn(registrations);

    // When
    List<Registration> foundRegistrations = service.findAllByCustomerId(customerId);

    // Then
    assertEquals(registrations.size(), foundRegistrations.size());
  }

  @Test
  void findAllByEventId() {
    // Given
    List<Registration> registrations = List.of(
        new Registration(customers.get(0), events.get(0)),
        new Registration(customers.get(1), events.get(0))
    );
    int eventId = events.get(0).getId();
    when(repository.findAllByEventId(eventId)).thenReturn(registrations);

    // When
    List<Registration> foundRegistrations = service.findAllByEventId(eventId);

    // Then
    assertEquals(registrations.size(), foundRegistrations.size());
  }

  @Test
  void deleteById() {
    // Given
    Registration registration = new Registration(1, customers.get(0), events.get(0));

    // When
    service.deleteById(registration.getId());

    // Then
    verify(repository, times(1)).deleteById(registration.getId());
  }

  @Test
  void createRegistration() throws DuplicateElementsException {
    // Given
    Registration registration = new Registration(customers.get(0), events.get(0));
    when(repository.save(registration)).thenReturn(registration);

    // When
    Registration savedRegistration = service.createRegistration(registration);

    // Then
    assertAll(
        () -> assertEquals(registration.getCustomer().getName(),
            savedRegistration.getCustomer().getName()),
        () -> assertEquals(registration.getEvent().getName(),
            savedRegistration.getEvent().getName())
    );
  }

  @Test
  void createRegistration_ShouldThrowWhenDuplicate() {
    // Given
    Registration registration = new Registration(customers.get(0), events.get(0));
    when(repository.save(registration)).thenThrow(DataIntegrityViolationException.class);

    // When
    Executable executable = () -> service.createRegistration(registration);

    // Then
    assertThrows(DuplicateElementsException.class, executable);
  }
}