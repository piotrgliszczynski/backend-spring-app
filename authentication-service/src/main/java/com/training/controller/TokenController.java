package com.training.controller;

import com.training.domain.Customer;
import com.training.domain.dto.TokenDto;
import com.training.service.CustomerClientService;
import com.training.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

  private final CustomerClientService clientService;
  private final JwtService jwtService;

  @PostMapping
  public ResponseEntity<Object> createJwtToken(@RequestBody Customer customer) {
    Optional<Customer> foundCustomer = clientService.getCustomerByEmail(customer.getEmail());

    if (foundCustomer.isPresent()
        && foundCustomer.get().getPassword().equals(customer.getPassword())) {
      String token = jwtService.createToken(customer);
      return ResponseEntity
          .status(HttpStatus.CREATED)
          .body(new TokenDto(token, "JWT"));
    }

    return ResponseEntity.badRequest().body("Invalid credentials");
  }
}
