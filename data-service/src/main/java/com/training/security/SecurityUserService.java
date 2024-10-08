package com.training.security;

import com.training.domain.Customer;
import com.training.exception.ElementNotFoundException;
import com.training.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class SecurityUserService implements UserDetailsService {

  private final CustomerService service;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Customer foundCustomer;
    try {
      foundCustomer = service.getCustomerByEmail(username);
    } catch (ElementNotFoundException e) {
      throw new UsernameNotFoundException(e.getMessage());
    }
    return new User(foundCustomer.getEmail(), foundCustomer.getPassword(), Collections.emptyList());
  }
}
