package com.training.controller;

import com.training.domain.Customer;
import com.training.domain.dto.CustomerDto;
import com.training.exception.DuplicateElementsException;
import com.training.mapper.CustomerMapper;
import com.training.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService service;
  private final CustomerMapper mapper;

  @GetMapping
  public ResponseEntity<List<CustomerDto>> getAll(
      @RequestParam(name = "name_like", required = false) String nameFilter
  ) {
    List<Customer> foundCustomers = service.getAllCustomers(nameFilter);
    List<CustomerDto> mappedCustomers = mapper.mapToDtoList(foundCustomers);
    return ResponseEntity.ok(mappedCustomers);
  }

  @PostMapping
  public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto)
      throws DuplicateElementsException {
    Customer customer = mapper.map(customerDto);
    Customer createdCustomer = service.createCustomer(customer);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(mapper.mapToDto(createdCustomer));
  }

  @PutMapping
  public ResponseEntity<CustomerDto> updateCustomer(@RequestBody CustomerDto customerDto) {
    Customer customer = mapper.map(customerDto);
    Customer updatedCustomer = service.updateCustomer(customer);
    return ResponseEntity.ok(mapper.mapToDto(updatedCustomer));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteCustomer(@PathVariable int id) {
    service.deleteById(id);
    return ResponseEntity.noContent().build();
  }

}
