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
  @GeneratedValue
  @NotNull
  @Column(name = "ID", unique = true)
  private Integer id;

  @Column(name = "EMAIL", unique = true)
  private String email;

  @Column(name = "NAME")
  private String name;

  @Column(name = "PASSWORD")
  private String password;

}
