package com.training.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class EventDto {

  private int id;
  private String name;
  private LocalDateTime date;
}
