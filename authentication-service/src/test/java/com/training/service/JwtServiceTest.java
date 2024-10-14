package com.training.service;

import com.training.domain.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@PropertySource("classpath:secure.properties")
class JwtServiceTest {

  @Autowired
  private JwtService service;

  @Value("${security.jwt.secret-key}")
  private String secretKey;
  @Value("${auth.api.user}")
  private String apiUser;


  @Test
  void createToken() {
    // Given
    Customer customer = new Customer("test@test.com", "test");

    // When
    String token = service.createToken(customer);
    Claims payload = Jwts.parser()
        .verifyWith(getSignInKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
    String username = payload.getSubject();
    Date expiration = payload.getExpiration();

    // Then
    assertAll(
        () -> assertEquals(customer.getEmail(), username),
        () -> assertTrue(expiration.after(new Date(System.currentTimeMillis())))
    );
  }

  @Test
  void createApiToken() {
    // Given
    String expectedUser = apiUser;

    // When
    String token = service.createApiToken();
    Claims payload = Jwts.parser()
        .verifyWith(getSignInKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
    String username = payload.getSubject();
    Date expiration = payload.getExpiration();

    // Then
    assertAll(
        () -> assertEquals(expectedUser, username),
        () -> assertTrue(expiration.after(new Date(System.currentTimeMillis())))
    );
  }

  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}