package com.training.mapper;

import com.training.domain.Registration;
import com.training.domain.dto.RegistrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationMapper {

  private final CustomerMapper customerMapper;
  private final EventMapper eventMapper;

  public List<RegistrationDto> mapToDtoList(List<Registration> registrations) {
    return registrations.stream()
        .map(this::mapToDto)
        .toList();
  }

  public RegistrationDto mapToDto(Registration registration) {
    return new RegistrationDto(
        registration.getId(),
        customerMapper.mapToRegistrationDto(registration.getCustomer()),
        eventMapper.mapToDto(registration.getEvent())
    );
  }
}
