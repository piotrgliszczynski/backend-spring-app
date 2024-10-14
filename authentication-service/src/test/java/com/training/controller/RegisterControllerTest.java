package com.training.controller;

import com.google.gson.Gson;
import com.training.domain.dto.CustomerDto;
import com.training.service.CustomerClientService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(RegisterController.class)
class RegisterControllerTest {

  private final static String URL = "/account/register";

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private CustomerClientService service;

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