package com.training.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REGISTRATIONS", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"CUSTOMER_ID", "EVENT_ID"})
})
public class Registration {

  @Id
  @GeneratedValue
  @Column(name = "ID")
  private int id;

  @ManyToOne
  @JoinColumn(name = "CUSTOMER_ID")
  private Customer customer;

  @ManyToOne
  @JoinColumn(name = "EVENT_ID")
  private Event event;

  public Registration(Customer customer, Event event) {
    this.customer = customer;
    this.event = event;
  }
}
