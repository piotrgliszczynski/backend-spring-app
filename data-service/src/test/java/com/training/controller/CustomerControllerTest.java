package com.training.controller;

import com.google.gson.Gson;
import com.training.domain.Customer;
import com.training.domain.dto.CustomerDto;
import com.training.exception.DuplicateElementsException;
import com.training.mapper.CustomerMapper;
import com.training.service.CustomerService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitWebConfig
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

  private final static String URL = "/api/customers";
  private final Gson gson = new Gson();
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private CustomerService service;
  @MockBean
  private CustomerMapper mapper;

  @Test
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
            .content(gson.toJson(customer1)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(customer1.getId())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(customer1.getName())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(customer1.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.password", Matchers.is(customer1.getPassword())));
  }

  @Test
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
            .content(gson.toJson(customer1)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.containsString("already exists!")));
  }

  @Test
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
            .content(gson.toJson(customer1)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(customer1.getId())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(customer1.getName())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(customer1.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.password", Matchers.is(customer1.getPassword())));
  }

  @Test
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