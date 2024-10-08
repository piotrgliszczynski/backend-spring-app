package com.training.service;

import com.training.client.CustomerClient;
import com.training.domain.Customer;
import com.training.domain.dto.CustomerDto;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class CustomerClientServiceTest {

  @Autowired
  private CustomerClientService service;
  @MockBean
  private CustomerClient client;

  @Test
  void shouldFetchCustomer() {
    // Given
    Customer customer = new Customer("test@test.com", "test");
    when(client.getCustomerByEmail(eq(customer.getEmail()), any())).thenReturn(customer);

    // When
    Optional<Customer> foundCustomer = service.getCustomerByEmail(customer.getEmail());

    // Then
    assertTrue(foundCustomer.isPresent());
    assertAll(
        () -> assertEquals(customer.getEmail(), foundCustomer.get().getEmail()),
        () -> assertEquals(customer.getPassword(), foundCustomer.get().getPassword())
    );
  }

  @Test
  void shouldFetchEmptyCustomer() {
    // Given
    Customer customer = new Customer("test@test.com", "test");
    when(client.getCustomerByEmail(eq(customer.getEmail()), any()))
        .thenThrow(FeignException.BadRequest.class);

    // When
    Optional<Customer> foundCustomer = service.getCustomerByEmail(customer.getEmail());

    // Then
    assertTrue(foundCustomer.isEmpty());
  }

  @Test
  void shouldCreateCustomer() {
    // Given
    CustomerDto customer = new CustomerDto("test", "test@test.com", "test");
    when(client.createCustomer(eq(customer), any())).thenReturn(customer);

    // When
    Optional<CustomerDto> foundCustomer = service.createCustomer(customer);

    // Then
    assertTrue(foundCustomer.isPresent());
    assertAll(
        () -> assertEquals(customer.getName(), foundCustomer.get().getName()),
        () -> assertEquals(customer.getEmail(), foundCustomer.get().getEmail()),
        () -> assertEquals(customer.getPassword(), foundCustomer.get().getPassword())
    );
  }

  @Test
  void shouldCreateEmptyCustomer() {
    // Given
    CustomerDto customer = new CustomerDto("test", "test@test.com", "test");
    when(client.createCustomer(eq(customer), any())).thenThrow(FeignException.BadRequest.class);

    // When
    Optional<CustomerDto> foundCustomer = service.createCustomer(customer);

    // Then
    assertTrue(foundCustomer.isEmpty());
  }

}