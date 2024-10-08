package com.training.service;

import com.training.domain.Customer;
import com.training.exception.ElementNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;

@Service
@PropertySource("classpath:secure.properties")
@RequiredArgsConstructor
public class JwtService {

  private final CustomerService service;
  @Value("${security.jwt.secret-key}")
  private String secretKey;
  @Value("${auth.api.user}")
  private String apiUser;

  public UserDetails buildUser(String token) throws ElementNotFoundException {
    String user = getUser(token);

    if (user.equals(apiUser)) {
      return new User(apiUser, apiUser, Collections.emptyList());
    }

    Customer customer = service.getCustomerByEmail(user);
    return new User(customer.getEmail(), customer.getPassword(), Collections.emptyList());
  }

  public boolean verify(String token) {
    return verifyExpiration(token) && verifyUser(token);
  }

  public boolean verifyUser(String token) {
    if (getUser(token).equals(apiUser)) {
      return true;
    }
    try {
      String email = service.getCustomerByEmail(getUser(token)).getEmail();
      return getUser(token).equals(email);
    } catch (ElementNotFoundException e) {
      return false;
    }
  }

  private String getUser(String token) {
    return getTokenBody(token).getSubject();
  }

  public boolean verifyExpiration(String token) {
    try {
      return getTokenBody(token).getExpiration().after(new Date());
    } catch (ExpiredJwtException e) {
      return false;
    }
  }

  private Claims getTokenBody(String token) {
    return Jwts.parser()
        .verifyWith(getSignInKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
