package com.training.repository;

import com.training.domain.Registration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends CrudRepository<Registration, Integer> {

  List<Registration> findAllByCustomerId(int customerId);

  List<Registration> findAllByEventId(int eventId);
}
