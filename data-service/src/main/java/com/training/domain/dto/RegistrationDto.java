package com.training.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {

  private int id;
  private RegistrationCustomerDto customer;
  private EventDto event;
}
