package com.training.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.training.domain.Customer;
import com.training.domain.dto.CustomerDto;
import com.training.exception.DuplicateElementsException;
import com.training.exception.ElementNotFoundException;
import com.training.mapper.CustomerMapper;
import com.training.mapper.LocalDateTimeTypeAdapter;
import com.training.service.CustomerService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
class CustomerControllerTest {

  private final static String URL = "/api/customers";
  private final static Gson GSON = new GsonBuilder()
      .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
      .create();

  private MockMvc mockMvc;
  @Autowired
  private WebApplicationContext context;
  @MockBean
  private CustomerService service;
  @MockBean
  private CustomerMapper mapper;

  @BeforeEach
  void securitySetup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(springSecurity()).
        build();
  }

  @Test
  @WithMockUser(username = "test@test.com", password = "test")
  void getAll_WithoutFilter() throws Exception {
    // Given
    Customer customer1 = new Customer(1, "test@test.com", "test1", "test1");
    Customer customer2 = new Customer(2, "test@test.com", "test2", "test2");
    CustomerDto customerDto1 = new CustomerDto(1, "test@test.com", "test1", "test1");
    CustomerDto customerDto2 = new CustomerDto(2, "test@test.com", "test2", "test2");
    List<Customer> customers = List.of(customer1, customer2);
    List<CustomerDto> customerDtos = List.of(customerDto1, customerDto2);

    when(service.getAllCustomers(null)).thenReturn(customers);
    when(mapper.mapToDtoList(customers)).thenReturn(customerDtos);

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders
            .get(URL))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
  }

  @Test
  @WithMockUser(username = "test@test.com", password = "test")
  void getAll_WithFilter() throws Exception {
    // Given
    Customer customer1 = new Customer(1, "test@test.com", "test1", "test1");
    CustomerDto customerDto1 = new CustomerDto(1, "test@test.com", "test1", "test1");
    List<Customer> customers = List.of(customer1);
    List<CustomerDto> customerDtos = List.of(customerDto1);

    when(service.getAllCustomers(customer1.getName())).thenReturn(customers);
    when(mapper.mapToDtoList(customers)).thenReturn(customerDtos);

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders
            .get(URL)
            .queryParam("name_like", customer1.getName()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));
  }

  @Test
  @WithMockUser(username = "test@test.com", password = "test")
  void getByEmail() throws Exception {
    // Given
    Customer customer1 = new Customer(1, "test@test.com", "test1", "test1");
    CustomerDto customerDto1 = new CustomerDto(1, "test@test.com", "test1", "test1");

    when(service.getCustomerByEmail(customer1.getEmail())).thenReturn(customer1);
    when(mapper.mapToDto(customer1)).thenReturn(customerDto1);

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders
            .get(URL + "/{email}", customer1.getEmail()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(customer1.getName())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(customer1.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.password", Matchers.is(customer1.getPassword())));
  }

  @Test
  @WithMockUser(username = "test@test.com", password = "test")
  void getByEmail_ThrowWhenNotExists() throws Exception {
    // Given
    Customer customer1 = new Customer(1, "test@test.com", "test1", "test1");

    when(service.getCustomerByEmail(customer1.getEmail())).thenThrow(new ElementNotFoundException("Test message"));

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders
            .get(URL + "/{email}", customer1.getEmail()))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.is("Test message")));
  }

  @Test
  @WithMockUser(username = "test@test.com", password = "test")
  void createCustomer() throws Exception {
    // Given
    Customer customer1 = new Customer(1, "test@test.com", "test1", "test1");
    CustomerDto customerDto1 = new CustomerDto(1, "test@test.com", "test1", "test1");

    when(mapper.map(customerDto1)).thenReturn(customer1);
    when(service.createCustomer(customer1)).thenReturn(customer1);
    when(mapper.mapToDto(any())).thenReturn(customerDto1);

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders
            .post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(customer1)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(customer1.getId())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(customer1.getName())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(customer1.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.password", Matchers.is(customer1.getPassword())));
  }

  @Test
  @WithMockUser(username = "test@test.com", password = "test")
  void createCustomer_DuplicateCustomer() throws Exception {
    // Given
    Customer customer1 = new Customer(1, "test@test.com", "test1", "test1");
    CustomerDto customerDto1 = new CustomerDto(1, "test@test.com", "test1", "test1");

    when(mapper.map(customerDto1)).thenReturn(customer1);
    when(service.createCustomer(any())).thenThrow(new DuplicateElementsException("The customer " +
        "with email " + customer1.getEmail() + " already exists!"));

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders
            .post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(customer1)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.containsString("already exists!")));
  }

  @Test
  @WithMockUser(username = "test@test.com", password = "test")
  void updateCustomer() throws Exception {    // Given
    Customer customer1 = new Customer(1, "test@test.com", "test1", "test1");
    CustomerDto customerDto1 = new CustomerDto(1, "test@test.com", "test1", "test1");

    when(mapper.map(customerDto1)).thenReturn(customer1);
    when(service.createCustomer(customer1)).thenReturn(customer1);
    when(mapper.mapToDto(any())).thenReturn(customerDto1);

    // When + Then
    mockMvc.perform(MockMvcRequestBuilders
            .put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(customer1)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(customer1.getId())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(customer1.getName())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(customer1.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.password", Matchers.is(customer1.getPassword())));
  }

  @Test
  @WithMockUser(username = "test@test.com", password = "test")
  void deleteCustomer() throws Exception {
    // Given
    int id = 1;

    // When
    mockMvc.perform(MockMvcRequestBuilders
            .delete(URL + "/{id}", id))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    // Then
    verify(service, times(1)).deleteById(id);
  }
}