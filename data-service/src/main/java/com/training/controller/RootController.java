package com.training.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

  private final static String STATUS_MESSAGE = "Data Service is up and running";

  @GetMapping
  public ResponseEntity<String> getStatus() {
    return ResponseEntity.ofNullable(STATUS_MESSAGE);
  }
}
