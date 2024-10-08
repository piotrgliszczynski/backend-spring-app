package com.training.service;

import com.training.domain.Customer;
import com.training.exception.ElementNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class SecurityUserServiceTest {

  @Autowired
  private SecurityUserService userService;
  @MockBean
  private CustomerService service;

  @Test
  void loadUserByUsername() throws ElementNotFoundException {
    // Given
    Customer customer = new Customer("test@test.com", "test", "test");
    when(service.getCustomerByEmail(customer.getEmail())).thenReturn(customer);

    // When
    UserDetails foundCustomer = userService.loadUserByUsername(customer.getEmail());

    // Then
    assertAll(
        () -> assertEquals(customer.getPassword(), foundCustomer.getPassword()),
        () -> assertEquals(customer.getPassword(), foundCustomer.getPassword()),
        () -> assertTrue(foundCustomer.getAuthorities().isEmpty())
    );
  }

  @Test
  void shouldThrow_WhenUsernameNotFound() throws ElementNotFoundException {
    // Given
    Customer customer = new Customer("test@test.com", "test", "test");
    when(service.getCustomerByEmail(customer.getEmail())).thenThrow(ElementNotFoundException.class);

    // When
    Executable executable = () -> userService.loadUserByUsername(customer.getEmail());

    // Then
    assertThrows(UsernameNotFoundException.class, executable);
  }
}