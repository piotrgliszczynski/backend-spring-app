package com.training.service;

import com.training.domain.Registration;
import com.training.exception.DuplicateElementsException;
import com.training.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {

  private final RegistrationRepository repository;

  public List<Registration> findAllByCustomerId(int customerId) {
    return repository.findAllByCustomerId(customerId);
  }

  public List<Registration> findAllByEventId(int eventId) {
    return repository.findAllByEventId(eventId);
  }

  public void deleteById(int id) {
    repository.deleteById(id);
  }

  public Registration createRegistration(Registration registration) throws DuplicateElementsException {
    try {
      return repository.save(registration);
    } catch (DataIntegrityViolationException ex) {
      throw new DuplicateElementsException("Cannot create duplicate registration!");
    }
  }
}
