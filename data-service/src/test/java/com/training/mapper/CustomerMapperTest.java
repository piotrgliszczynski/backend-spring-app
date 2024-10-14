package com.training.mapper;

import com.training.domain.Customer;
import com.training.domain.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CustomerMapperTest {

  @Autowired
  private CustomerMapper mapper;

  private CustomerDto customerDto;
  private Customer customer;

  @BeforeEach
  void createData() {
    customerDto = new CustomerDto(1, "test", "test@test.com", "test");
    customer = new Customer(1, "test", "test@test.com", "test");
  }

  @Test
  void mapToDtoList() {
    // Given
    List<Customer> customers = List.of(
        new Customer(1, "test1", "test1@test.com", "test1"),
        new Customer(2, "test2", "test2@test.com", "test2")
    );

    // When
    List<CustomerDto> customerDtos = mapper.mapToDtoList(customers);

    // Then
    assertAll(
        () -> assertEquals(customers.size(), customerDtos.size())
    );
  }

  @Test
  void mapToDto() {
    // Given

    // When
    CustomerDto actualDto = mapper.mapToDto(customer);

    // Then
    assertAll(
        () -> assertEquals(customerDto.getId(), actualDto.getId()),
        () -> assertEquals(customerDto.getEmail(), actualDto.getEmail()),
        () -> assertEquals(customerDto.getName(), actualDto.getName()),
        () -> assertEquals(customerDto.getPassword(), actualDto.getPassword())
    );
  }

  @Test
  void map() {
    // Given

    // When
    Customer actualCustomer = mapper.map(customerDto);

    // Then
    assertAll(
        () -> assertEquals(customer.getId(), actualCustomer.getId()),
        () -> assertEquals(customer.getEmail(), actualCustomer.getEmail()),
        () -> assertEquals(customer.getName(), actualCustomer.getName()),
        () -> assertEquals(customer.getPassword(), actualCustomer.getPassword())
    );
  }
}