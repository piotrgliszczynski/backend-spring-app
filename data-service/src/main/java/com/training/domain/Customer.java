package com.training.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CUSTOMERS")
public class Customer {

  @Id
  @GeneratedValue(generator = "customer_seq")
  @SequenceGenerator(name = "customer_seq", sequenceName = "CUSTOMER_SEQ", initialValue = 1000,
      allocationSize = 1)
  @NotNull
  @Column(name = "ID", unique = true)
  private Integer id;

  @Column(name = "EMAIL", unique = true)
  private String email;

  @Column(name = "NAME")
  private String name;

  @Column(name = "PASSWORD")
  private String password;

  public Customer(String email, String name, String password) {
    this.email = email;
    this.name = name;
    this.password = password;
  }
}
