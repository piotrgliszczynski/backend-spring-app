package com.training.controller;

import com.google.gson.Gson;
import com.training.domain.dto.CustomerDto;
import com.training.service.CustomerClientService;
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
class RegisterControllerTest {

  private static final String URL = "/account/register";

  private MockMvc mockMvc;
  @Autowired
  private WebApplicationContext context;
  @MockBean
  private CustomerClientService service;

  @BeforeEach
  void securitySetup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(springSecurity()).
        build();
  }

  @Test
  void shouldRegisterCustomer() throws Exception {
    // Given
    CustomerDto customer = new CustomerDto("test", "test@test.com", "test");
    when(service.createCustomer(any())).thenReturn(Optional.of(customer));

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders
            .post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new Gson().toJson(customer)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(customer.getName())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(customer.getEmail())))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.password",
                Matchers.is(customer.getPassword())));
  }

  @Test
  void shouldThrowError_WhenCustomerExists() throws Exception {
    // Given
    CustomerDto customer = new CustomerDto("test", "test@test.com", "test");
    when(service.createCustomer(any())).thenReturn(Optional.empty());

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders
            .post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new Gson().toJson(customer)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }
}