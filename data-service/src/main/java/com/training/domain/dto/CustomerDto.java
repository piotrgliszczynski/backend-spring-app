package com.training.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

  private Integer id;
  private String email;
  private String name;
  private String password;

}
