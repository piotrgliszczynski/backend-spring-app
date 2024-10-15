package com.training.security;

import com.training.domain.Customer;
import com.training.service.CustomerClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FeignAuthenticationProvider implements AuthenticationProvider {

  private final CustomerClientService customerService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    Optional<Customer> foundCustomer = customerService.getCustomerByEmail(authentication.getName());
    Customer customer = foundCustomer
        .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
    return new UsernamePasswordAuthenticationToken(
        customer.getEmail(),
        customer.getPassword(),
        Collections.emptyList());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
