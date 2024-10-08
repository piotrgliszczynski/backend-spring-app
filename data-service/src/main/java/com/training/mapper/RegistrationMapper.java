package com.training.mapper;

import com.training.domain.Registration;
import com.training.domain.dto.RegistrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationMapper {

  private final CustomerMapper customerMapper;
  private final EventMapper eventMapper;

  public RegistrationDto mapToDto(Registration registration) {
    return new RegistrationDto(
        registration.getId(),
        customerMapper.mapToRegistrationDto(registration.getCustomer()),
        eventMapper.mapToDto(registration.getEvent())
    );
  }
}
