package com.training.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@Getter
@PropertySource("classpath:secure.properties")
@RequiredArgsConstructor
public class TokenDto {

  @JsonProperty("access_token")
  private final String token;

  @JsonProperty("token_type")
  private final String type = "JWT";

  @JsonProperty("expires_in")
  @Value("${security.jwt.expiration-time}")
  private long expiration;
}
