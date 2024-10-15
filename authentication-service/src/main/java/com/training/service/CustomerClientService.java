package com.training.service;

import com.training.client.CustomerClient;
import com.training.domain.Customer;
import com.training.domain.dto.CustomerDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerClientService {

  private static final String BEARER = "Bearer";

  private final CustomerClient client;
  private final JwtService jwtService;

  public Optional<Customer> getCustomerByEmail(String email) {
    String token = jwtService.createApiToken();
    Customer customer = null;

    try {
      customer = client.getCustomerByEmail(email, createAuthorizationHeader(token));
    } catch (FeignException ex) {
      log.error(ex.getMessage());
    }
    return Optional.ofNullable(customer);
  }

  public Optional<CustomerDto> createCustomer(CustomerDto customerDto) {
    String token = jwtService.createApiToken();
    CustomerDto customer = null;
    try {
      customer = client.createCustomer(customerDto, createAuthorizationHeader(token));
    } catch (FeignException ex) {
      log.error(ex.getMessage());
    }
    return Optional.ofNullable(customer);
  }

  private String createAuthorizationHeader(String token) {
    return BEARER + " " + token;
  }

}
