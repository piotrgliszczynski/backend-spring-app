package com.training.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

  private final static String STATUS_MESSAGE = "Authentication Service is up and running";

  @GetMapping
  public ResponseEntity<String> getStatus() {
    return ResponseEntity.ok(STATUS_MESSAGE);
  }

}
