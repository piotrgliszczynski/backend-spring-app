package com.training.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EVENTS")
public class Event {

  @Id
  @GeneratedValue(generator = "event_seq")
  @SequenceGenerator(name = "event_seq", sequenceName = "EVENT_SEQ", initialValue = 100,
      allocationSize = 1)
  @Column(name = "ID", unique = true)
  private int id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "DATE")
  private LocalDateTime date;

  @OneToMany(
      targetEntity = Registration.class,
      mappedBy = "event",
      cascade = CascadeType.REMOVE,
      fetch = FetchType.LAZY
  )
  private List<Registration> registrations;

  public Event(String name, LocalDateTime date) {
    this.name = name;
    this.date = date;
  }

  public Event(int id, String name, LocalDateTime date) {
    this.id = id;
    this.name = name;
    this.date = date;
  }
}
