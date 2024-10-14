package com.training.service;

import com.training.domain.Customer;
import com.training.exception.ElementNotFoundException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@PropertySource("classpath:secure.properties")
class JwtServiceTest {

  private final static String USER = "test";

  private String notExpired;
  private String expired;

  @Autowired
  private JwtService service;
  @MockBean
  private CustomerService customerService;

  @Value("${security.jwt.secret-key}")
  private String secretKey;

  @BeforeEach
  void buildToken() {
    notExpired = Jwts
        .builder()
        .subject(USER)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 3600000))
        .signWith(getSignInKey())
        .compact();

    expired = Jwts
        .builder()
        .subject("test")
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1))
        .signWith(getSignInKey())
        .compact();
  }

  @Test
  void getUserDetails() throws ElementNotFoundException {
    // Given
    UserDetails expected = new User(USER, "test", Collections.emptyList());
    Customer customer = new Customer(USER, "test", "test");
    when(customerService.getCustomerByEmail(USER)).thenReturn(customer);

    // When
    UserDetails actualUser = service.buildUser(notExpired);

    // Then
    assertAll(
        () -> assertEquals(actualUser.getUsername(), expected.getUsername()),
        () -> assertEquals(actualUser.getPassword(), expected.getPassword()),
        () -> assertTrue(actualUser.getAuthorities().isEmpty())
    );
  }

  @Test
  void verify_ShouldBeValid() throws ElementNotFoundException {
    // Given
    Customer userDetails = new Customer(USER, "test", "test");
    when(customerService.getCustomerByEmail(USER)).thenReturn(userDetails);

    // When
    boolean valid = service.verify(notExpired);

    // Then
    assertTrue(valid);
  }

  @Test
  void verify_ShouldBeInValidUser() throws ElementNotFoundException {
    // Given
    when(customerService.getCustomerByEmail(USER)).thenThrow(ElementNotFoundException.class);

    // When
    boolean valid = service.verify(notExpired);

    // Then
    assertFalse(valid);
  }

  @Test
  void verify_ShouldBeExpired() throws InterruptedException, ElementNotFoundException {
    // Given
    Thread.sleep(2);
    Customer userDetails = new Customer(USER, "test", "test");
    when(customerService.getCustomerByEmail(USER)).thenReturn(userDetails);

    // When
    boolean valid = service.verify(expired);

    // Then
    assertFalse(valid);
  }

  @Test
  void verifyUser_ShouldBeValid() throws ElementNotFoundException {
    // Given
    Customer userDetails = new Customer(USER, "test", "test");
    when(customerService.getCustomerByEmail(USER)).thenReturn(userDetails);

    // When
    boolean valid = service.verifyUser(notExpired);

    // Then
    assertTrue(valid);
  }

  @Test
  void verifyUser_ShouldBeInValid() throws ElementNotFoundException {
    // Given
    when(customerService.getCustomerByEmail(USER)).thenThrow(ElementNotFoundException.class);

    // When
    boolean valid = service.verifyUser(notExpired);

    // Then
    assertFalse(valid);
  }

  @Test
  void verifyExpiration_NotExpired() {
    // Given

    // When
    boolean valid = service.verifyExpiration(notExpired);

    // Then
    assertTrue(valid);
  }

  @Test
  void verifyExpiration_Expired() throws InterruptedException {
    // Given
    Thread.sleep(2);

    // When
    boolean valid = service.verifyExpiration(expired);

    // Then
    assertFalse(valid);
  }

  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}