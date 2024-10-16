package com.training.controller;

import com.training.domain.Customer;
import com.training.domain.Event;
import com.training.domain.Registration;
import com.training.domain.dto.RegistrationDto;
import com.training.exception.DuplicateElementsException;
import com.training.exception.ElementNotFoundException;
import com.training.mapper.RegistrationMapper;
import com.training.service.CustomerService;
import com.training.service.EventService;
import com.training.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registrations")
@RequiredArgsConstructor
public class RegistrationController {

  private final RegistrationService registrationService;
  private final CustomerService customerService;
  private final EventService eventService;
  private final RegistrationMapper registrationMapper;

  @GetMapping("/customer/{customerId}")
  public ResponseEntity<List<RegistrationDto>> getRegistrationsByCustomer(
      @PathVariable int customerId) {
    List<Registration> registrations = registrationService.findAllByCustomerId(customerId);
    List<RegistrationDto> registrationDtos = registrationMapper.mapToDtoList(registrations);
    return ResponseEntity.ok(registrationDtos);
  }

  @GetMapping("/event/{eventId}")
  public ResponseEntity<List<RegistrationDto>> getRegistrationsByEvent(@PathVariable int eventId) {
    List<Registration> registrations = registrationService.findAllByEventId(eventId);
    List<RegistrationDto> registrationDtos = registrationMapper.mapToDtoList(registrations);
    return ResponseEntity.ok(registrationDtos);
  }

  @PostMapping("/event/{eventId}")
  public ResponseEntity<RegistrationDto> createRegistrationForCustomer(
      @PathVariable int eventId,
      Authentication authentication) throws ElementNotFoundException, DuplicateElementsException {
    String customerEmail = authentication.getName();

    Customer customer = customerService.getCustomerByEmail(customerEmail);
    Event event = eventService.getById(eventId);

    Registration savedRegistration = registrationService.createRegistration(
        new Registration(customer, event)
    );
    RegistrationDto registrationDto = registrationMapper.mapToDto(savedRegistration);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(registrationDto);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteRegistration(@PathVariable int id) {
    registrationService.deleteById(id);
    return ResponseEntity.ok().build();
  }

}
