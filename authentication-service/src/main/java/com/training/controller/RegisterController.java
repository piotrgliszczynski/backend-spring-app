package com.training.controller;

import com.training.domain.dto.CustomerDto;
import com.training.service.CustomerClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

  private final CustomerClientService service;

  @PostMapping
  public ResponseEntity<Object> registerCustomer(@RequestBody CustomerDto customerDto) {
    Optional<CustomerDto> createdCustomer = service.createCustomer(customerDto);

    if (createdCustomer.isPresent()) {
      return ResponseEntity
          .status(HttpStatus.CREATED)
          .body(createdCustomer.get());
    }

    return ResponseEntity.badRequest().body("Error during registration, please try again later");
  }

}
