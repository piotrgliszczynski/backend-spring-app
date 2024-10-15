package com.training.mapper;

import com.training.domain.Customer;
import com.training.domain.dto.CustomerDto;
import com.training.domain.dto.RegistrationCustomerDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerMapper {

  public List<CustomerDto> mapToDtoList(List<Customer> customers) {
    return customers.stream()
        .map(this::mapToDto)
        .toList();
  }

  public CustomerDto mapToDto(Customer customer) {
    return new CustomerDto(
        customer.getId(),
        customer.getEmail(),
        customer.getName(),
        customer.getPassword()
    );
  }

  public RegistrationCustomerDto mapToRegistrationDto(Customer customer) {
    return new RegistrationCustomerDto(
        customer.getId(),
        customer.getName(),
        customer.getEmail()
    );
  }

  public Customer map(CustomerDto customerDto) {
    return new Customer(
        customerDto.getId(),
        customerDto.getEmail(),
        customerDto.getName(),
        customerDto.getPassword()
    );
  }
}
