package com.training.repository;

import com.training.domain.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerRepositoryTest {

  @Autowired
  private CustomerRepository repository;

  @Test
  void shouldSaveCustomer() {
    // Given
    Customer customer = new Customer("test", "test@test.com", "test");

    // When
    repository.save(customer);

    // Then
    int id = customer.getId();
    Optional<Customer> savedCustomer = repository.findById(id);
    assertTrue(savedCustomer.isPresent());
    assertAll(
        () -> assertEquals(customer.getEmail(), savedCustomer.get().getEmail()),
        () -> assertEquals(customer.getName(), savedCustomer.get().getName()),
        () -> assertEquals(customer.getPassword(), savedCustomer.get().getPassword())
    );

    // CleanUp
    repository.deleteById(id);
  }

  @Test
  void shouldUpdateCustomer() {
    // Given
    Customer customer = new Customer("test", "test@test.com", "test");
    repository.save(customer);
    int id = customer.getId();

    Customer updatedCustomer = new Customer(id, "testChanged", "testChanged@test.com", "test");

    // When
    repository.save(updatedCustomer);

    // Then
    List<Customer> customers = repository.findAll();
    assertEquals(1, customers.size());
    assertAll(
        () -> assertEquals(updatedCustomer.getId(), customers.get(0).getId()),
        () -> assertEquals(updatedCustomer.getName(), customers.get(0).getName()),
        () -> assertEquals(updatedCustomer.getPassword(), customers.get(0).getPassword()),
        () -> assertEquals(updatedCustomer.getEmail(), customers.get(0).getEmail())
    );

    // CleanUp
    repository.deleteById(id);
  }

  @Test
  void shouldGetAllCustomers() {
    // Given
    Customer customer1 = new Customer("test1", "test1@test.com", "test1");
    Customer customer2 = new Customer("test2", "test2@test.com", "test2");
    List<Customer> customers = List.of(customer1, customer2);

    repository.saveAll(customers);
    List<Integer> ids = List.of(customer1.getId(), customer2.getId());

    // When
    List<Customer> foundCustomers = repository.findAll();

    // Then
    assertEquals(customers.size(), foundCustomers.size());

    // CleanUp
    repository.deleteAllById(ids);
  }

  @Test
  void shouldGetCustomerByEmail() {
    // Given
    Customer customer1 = new Customer("test1", "test1@test.com", "test1");
    Customer customer2 = new Customer("test2", "test2@test.com", "test2");
    List<Customer> customers = List.of(customer1, customer2);

    repository.saveAll(customers);
    List<Integer> ids = List.of(customer1.getId(), customer2.getId());

    // When
    Optional<Customer> foundCustomer = repository.findCustomerByEmail(customer1.getEmail());

    // Then
    assertTrue(foundCustomer.isPresent());
    assertAll(
        () -> assertEquals(customer1.getId(), foundCustomer.get().getId()),
        () -> assertEquals(customer1.getEmail(), foundCustomer.get().getEmail()),
        () -> assertEquals(customer1.getName(), foundCustomer.get().getName()),
        () -> assertEquals(customer1.getPassword(), foundCustomer.get().getPassword())
    );

    // CleanUp
    repository.deleteAllById(ids);
  }

  @Test
  void shouldNotSaveDuplicateEmail() {
    // Given
    Customer customer1 = new Customer("test@test.com", "test1", "test1");
    Customer customer2 = new Customer("test@test.com", "test2", "test2");
    repository.save(customer1);

    // When
    Executable executable = () -> repository.save(customer2);

    // Then
    assertThrows(DataIntegrityViolationException.class, executable);

    // CleanUp
    repository.deleteAllById(List.of(customer1.getId(), customer2.getId()));
  }
}