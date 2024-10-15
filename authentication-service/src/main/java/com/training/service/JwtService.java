package com.training.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@PropertySource("classpath:secure.properties")
public class JwtService {

  @Value("${security.jwt.secret-key}")
  private String secretKey;
  @Value("${security.jwt.expiration-time}")
  private long jwtExpiration;
  @Value("${auth.api.user}")
  private String apiUser;

  public String createToken(String username) {
    return Jwts
        .builder()
        .subject(username)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
        .signWith(getSignInKey())
        .compact();
  }

  public String createApiToken() {
    return Jwts
        .builder()
        .subject(apiUser)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
        .signWith(getSignInKey())
        .compact();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
