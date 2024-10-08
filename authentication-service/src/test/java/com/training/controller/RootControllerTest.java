package com.training.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
class RootControllerTest {

  private final static String URL = "/account";

  private MockMvc mockMvc;
  @Autowired
  private WebApplicationContext context;

  private static List<HttpMethod> provideDisallowedHttpMethods() {
    return List.of(HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH);
  }

  @BeforeEach
  void securitySetup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(springSecurity()).
        build();
  }

  @Test
  void shouldReturnStatus() throws Exception {
    // Given
    String expectedMessage = "up and running";

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders
            .get(URL))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.containsString(expectedMessage)));
  }

  @ParameterizedTest
  @MethodSource("provideDisallowedHttpMethods")
  void shouldReturn405Error(HttpMethod httpMethod) throws Exception {
    // Given
    String statusMessage = "is not supported";

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders.request(httpMethod, URL))
        .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed())
        .andExpect(MockMvcResultMatchers
            .jsonPath("$", Matchers.containsString(statusMessage)));
  }
}