package com.training.controller;

import com.training.domain.Customer;
import com.training.domain.dto.TokenDto;
import com.training.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

  private final JwtService jwtService;

  @PostMapping
  public ResponseEntity<Object> createJwtToken(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    Customer customer = new Customer(user.getUsername(), user.getPassword());

    String token = jwtService.createToken(customer);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(new TokenDto(token, "JWT"));
  }
}
