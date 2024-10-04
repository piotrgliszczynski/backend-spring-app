package com.training.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringJUnitWebConfig
@WebMvcTest(RootController.class)
class RootControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldGetStatus() throws Exception {
    // Given
    String statusUrl = "/api";
    String statusMessage = "Data Service is up and running";

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders
            .get(statusUrl))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.is(statusMessage)));
  }
}