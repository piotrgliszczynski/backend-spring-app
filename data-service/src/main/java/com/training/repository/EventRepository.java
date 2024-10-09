package com.training.repository;

import com.training.domain.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends CrudRepository<Event, Integer> {

  @Override
  Optional<Event> findById(Integer id);

  @Override
  List<Event> findAll();
}
