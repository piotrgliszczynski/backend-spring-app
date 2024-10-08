package com.training.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EVENTS")
public class Event {

  @Id
  @GeneratedValue
  @Column(name = "ID", unique = true)
  private int id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "DATE")
  private LocalDateTime date;
}
