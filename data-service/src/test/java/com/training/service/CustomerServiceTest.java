package com.training.service;

import com.training.domain.Customer;
import com.training.exception.DuplicateElementsException;
import com.training.exception.ElementNotFoundException;
import com.training.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

  @InjectMocks
  private CustomerService service;

  @Mock
  private CustomerRepository repository;

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"test"})
  void getAllCustomers_WhenFilterNull_Empty_Or_Partial(String filter) {
    // Given
    Customer customer1 = new Customer("test1@test.com", "test1", "test1");
    Customer customer2 = new Customer("test2@test.com", "test2", "test2");
    List<Customer> customers = List.of(customer1, customer2);

    when(repository.findAll()).thenReturn(customers);

    // When
    List<Customer> foundCustomers = service.getAllCustomers(filter);

    // Then
    assertEquals(customers, foundCustomers);
  }

  @Test
  void getAllCustomers_WhenFilterExact() {
    // Given
    Customer customer1 = new Customer("test1@test.com", "test1", "test1");
    Customer customer2 = new Customer("test2@test.com", "test2", "test2");
    List<Customer> customers = List.of(customer1, customer2);
    String filter = "test1";

    when(repository.findAll()).thenReturn(customers);

    // When
    List<Customer> foundCustomers = service.getAllCustomers(filter);

    // Then
    assertEquals(1, foundCustomers.size());
    assertAll(
        () -> assertEquals(customer1.getName(), foundCustomers.get(0).getName()),
        () -> assertEquals(customer1.getEmail(), foundCustomers.get(0).getEmail()),
        () -> assertEquals(customer1.getPassword(), foundCustomers.get(0).getPassword())
    );
  }

  @Test
  void getCustomerByEmail_WhenExists() throws ElementNotFoundException {
    // Given
    Customer customer1 = new Customer("test1@test.com", "test1", "test1");
    when(repository.findCustomerByEmail(customer1.getEmail())).thenReturn(Optional.of(customer1));

    // When
    Customer foundCustomer = service.getCustomerByEmail(customer1.getEmail());

    // Then
    assertAll(
        () -> assertEquals(customer1.getName(), foundCustomer.getName()),
        () -> assertEquals(customer1.getEmail(), foundCustomer.getEmail()),
        () -> assertEquals(customer1.getPassword(), foundCustomer.getPassword())
    );
  }

  @Test
  void getCustomerByEmail_WhenNotExists() {
    // Given
    String email = "test1@test.com";
    when(repository.findCustomerByEmail(anyString())).thenReturn(Optional.empty());

    // When
    Executable executable = () -> service.getCustomerByEmail(email);

    // Then
    assertThrows(ElementNotFoundException.class, executable);
  }

  @Test
  void createCustomer_NonExistingCustomer() throws DuplicateElementsException {
    // Given
    Customer customer1 = new Customer("test1@test.com", "test1", "test1");
    when(repository.save(customer1)).thenReturn(customer1);

    // When
    Customer createdCustomer = service.createCustomer(customer1);

    // Then
    assertAll(
        () -> assertEquals(customer1.getName(), createdCustomer.getName()),
        () -> assertEquals(customer1.getEmail(), createdCustomer.getEmail()),
        () -> assertEquals(customer1.getPassword(), createdCustomer.getPassword())
    );
  }

  @Test
  void createCustomer_DuplicateCustomer() {
    // Given
    Customer customer1 = new Customer("test1@test.com", "test1", "test1");
    when(repository.save(customer1)).thenThrow(DataIntegrityViolationException.class);

    // When
    Executable executable = () -> service.createCustomer(customer1);

    // Then
    assertThrows(DuplicateElementsException.class, executable);
  }

  @Test
  void updateCustomer() {
    // Given
    Customer customer1 = new Customer("test1@test.com", "test1", "test1");
    when(repository.save(customer1)).thenReturn(customer1);

    // When
    Customer updatedCustomer = service.updateCustomer(customer1);

    // Then
    assertAll(
        () -> assertEquals(customer1.getName(), updatedCustomer.getName()),
        () -> assertEquals(customer1.getEmail(), updatedCustomer.getEmail()),
        () -> assertEquals(customer1.getPassword(), updatedCustomer.getPassword())
    );
  }

  @Test
  void deleteById() {
    // Given
    int id = 1;

    // When
    service.deleteById(id);

    // Then
    verify(repository, times(1)).deleteById(id);
  }
}