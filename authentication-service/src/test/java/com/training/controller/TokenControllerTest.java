package com.training.controller;

import com.google.gson.Gson;
import com.training.domain.Customer;
import com.training.service.CustomerClientService;
import com.training.service.JwtService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
class TokenControllerTest {

  private final static String URL = "/account/token";

  private MockMvc mockMvc;
  @Autowired
  private WebApplicationContext context;

  @MockBean
  private CustomerClientService clientService;
  @MockBean
  private JwtService jwtService;

  @BeforeEach
  void securitySetup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(springSecurity()).
        build();
  }

  @Test
  void shouldCreateToken() throws Exception {
    // Given
    String token = "12345";
    Customer customer = new Customer("test@test.com", "test");
    when(clientService.getCustomerByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
    when(jwtService.createToken(any())).thenReturn(token);

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders
            .post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new Gson().toJson(customer)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.access_token", Matchers.is(token)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.token_type", Matchers.is("JWT")));
  }

  @Test
  void shouldReturnError_WhenEmptyCustomer() throws Exception {
    // Given
    Customer customer = new Customer("test@test.com", "test");
    when(clientService.getCustomerByEmail(customer.getEmail())).thenReturn(Optional.empty());

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders
            .post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new Gson().toJson(customer)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.is("Invalid credentials")));
  }

  @Test
  void shouldReturnError_WhenBadCredentials() throws Exception {
    // Given
    Customer customer = new Customer("test@test.com", "test");
    Customer otherCustomer = new Customer("test@test.com", "testOther");
    when(clientService.getCustomerByEmail(customer.getEmail())).thenReturn(Optional.of(otherCustomer));

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders
            .post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new Gson().toJson(customer)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.is("Invalid credentials")));
  }
}