package com.training.service;

import com.training.client.CustomerClient;
import com.training.domain.Customer;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerClientService {

  private final CustomerClient client;

  public Optional<Customer> getCustomerByEmail(String email) {
    Customer customer = null;
    try {
      customer = client.getCustomerByEmail(email);
    } catch (FeignException ex) {
      System.out.println(ex.getMessage());
    }
    return Optional.ofNullable(customer);
  }


}
