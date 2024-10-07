package com.training.service;

import com.training.domain.Customer;
import com.training.exception.DuplicateElementsException;
import com.training.exception.ElementNotFoundException;
import com.training.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository repository;

  public List<Customer> getAllCustomers(String nameFilter) {
    List<Customer> customers = repository.findAll();
    Optional<String> optionalNameFilter = Optional.ofNullable(nameFilter);
    String filter = optionalNameFilter.orElse("");

    return customers.stream()
        .filter(customer -> customer.getName().contains(filter.toLowerCase()))
        .toList();
  }

  public Customer getCustomerByEmail(String email) throws ElementNotFoundException {
    Optional<Customer> customer = repository.findCustomerByEmail(email);
    return customer.orElseThrow(() ->
        new ElementNotFoundException(
            "Could not find Customer with email " + email
        )
    );
  }

  public Customer createCustomer(Customer customer) throws DuplicateElementsException {
    try {
      return repository.save(customer);
    } catch (DataIntegrityViolationException e) {
      throw new DuplicateElementsException(
          "The customer with email " + customer.getEmail() + " already exists!");
    }
  }

  public Customer updateCustomer(Customer customer) {
    return repository.save(customer);
  }

  public void deleteById(int id) {
    repository.deleteById(id);
  }

}
