package com.training.repository;

import com.training.domain.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

  @Override
  List<Customer> findAll();

  Optional<Customer> findCustomerByEmail(String email);
}
