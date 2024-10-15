package com.training.repository;

import com.training.domain.Customer;
import com.training.domain.Event;
import com.training.domain.Registration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RegistrationRepositoryTest {

  @Autowired
  private RegistrationRepository repository;
  @Autowired
  private CustomerRepository customerRepository;
  @Autowired
  private EventRepository eventRepository;

  @Test
  void shouldSaveRegistration() {
    // Given
    Customer customer = new Customer("test@test.com", "test", "test");
    Event event = new Event("test", LocalDateTime.now());
    Registration registration = new Registration(customer, event);
    customerRepository.save(customer);
    eventRepository.save(event);

    // When
    repository.save(registration);
    int id = registration.getId();

    // Then
    Optional<Registration> foundRegistration = repository.findById(id);
    assertTrue(foundRegistration.isPresent());
    assertAll(
        () -> assertEquals(registration.getCustomer().getName(),
            foundRegistration.get().getCustomer().getName()),
        () -> assertEquals(registration.getCustomer().getEmail(),
            foundRegistration.get().getCustomer().getEmail()),
        () -> assertEquals(registration.getCustomer().getPassword(),
            foundRegistration.get().getCustomer().getPassword()),
        () -> assertEquals(registration.getEvent().getName(),
            foundRegistration.get().getEvent().getName())
    );

    // Cleanup
    repository.deleteById(id);
    eventRepository.deleteById(event.getId());
    customerRepository.deleteById(customer.getId());
  }

  @Test
  void shouldNotSaveDuplicateRegistration() {
    // Given
    Customer customer = new Customer("test@test.com", "test", "test");
    Event event = new Event("test", LocalDateTime.now());
    Registration registration1 = new Registration(customer, event);
    Registration registration2 = new Registration(customer, event);
    customerRepository.save(customer);
    eventRepository.save(event);
    repository.save(registration1);

    // When
    Executable executable = () -> repository.save(registration2);

    // Then
    assertThrows(DataIntegrityViolationException.class, executable);

    // Cleanup
    repository.deleteById(registration1.getId());
    repository.deleteById(registration2.getId());
    eventRepository.deleteById(event.getId());
    customerRepository.deleteById(customer.getId());
  }

  @Test
  void shouldDeleteRegistration() {
    // Given
    Customer customer = new Customer("test@test.com", "test", "test");
    Event event = new Event("test", LocalDateTime.now());
    Registration registration = new Registration(customer, event);
    customerRepository.save(customer);
    eventRepository.save(event);
    repository.save(registration);
    int id = registration.getId();

    // When
    repository.deleteById(id);

    // Then
    Optional<Registration> foundRegistration = repository.findById(id);
    Optional<Customer> foundCustomer = customerRepository.findById(customer.getId());
    Optional<Event> foundEvent = eventRepository.findById(event.getId());
    assertFalse(foundRegistration.isPresent());
    assertTrue(foundCustomer.isPresent());
    assertTrue(foundEvent.isPresent());

    // Cleanup
    eventRepository.deleteById(event.getId());
    customerRepository.deleteById(customer.getId());
  }

  @Test
  void shouldFindByCustomer() {
    // Given
    Customer customer1 = new Customer("test1@test.com", "test1", "test1");
    Customer customer2 = new Customer("test2@test.com", "test2", "test2");
    Event event1 = new Event("test1", LocalDateTime.now());
    Event event2 = new Event("test2", LocalDateTime.now());
    Registration registration1 = new Registration(customer1, event1);
    Registration registration2 = new Registration(customer1, event2);
    customerRepository.saveAll(List.of(customer1, customer2));
    eventRepository.saveAll(List.of(event1, event2));
    repository.saveAll(List.of(registration1, registration2));

    // When
    List<Registration> registrations = repository.findAllByCustomerId(customer1.getId());

    // Then
    assertEquals(2, registrations.size());
    assertAll(
        () -> assertEquals(customer1.getName(), registrations.get(0).getCustomer().getName()),
        () -> assertEquals(customer1.getName(), registrations.get(1).getCustomer().getName())
    );

    // Cleanup
    repository.deleteAllById(List.of(registration1.getId(), registration2.getId()));
    eventRepository.deleteAllById(List.of(event1.getId(), event2.getId()));
    customerRepository.deleteAllById(List.of(customer1.getId(), customer2.getId()));
  }

  @Test
  void shouldFindByEvent() {
    // Given
    Customer customer1 = new Customer("test1@test.com", "test1", "test1");
    Customer customer2 = new Customer("test2@test.com", "test2", "test2");
    Event event1 = new Event("test1", LocalDateTime.now());
    Event event2 = new Event("test2", LocalDateTime.now());
    Registration registration1 = new Registration(customer1, event1);
    Registration registration2 = new Registration(customer2, event1);
    customerRepository.saveAll(List.of(customer1, customer2));
    eventRepository.saveAll(List.of(event1, event2));
    repository.saveAll(List.of(registration1, registration2));

    // When
    List<Registration> registrations = repository.findAllByEventId(event1.getId());

    // Then
    assertEquals(2, registrations.size());
    assertAll(
        () -> assertEquals(event1.getName(), registrations.get(0).getEvent().getName()),
        () -> assertEquals(event1.getName(), registrations.get(1).getEvent().getName())
    );

    // Cleanup
    repository.deleteAllById(List.of(registration1.getId(), registration2.getId()));
    eventRepository.deleteAllById(List.of(event1.getId(), event2.getId()));
    customerRepository.deleteAllById(List.of(customer1.getId(), customer2.getId()));
  }

}