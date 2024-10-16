package com.training.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.gson.Gson;
import com.training.domain.Customer;
import com.training.domain.dto.CustomerDto;
import feign.FeignException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CustomerClientTest.FeignConfig.class)
class CustomerClientTest {

  private static final String TOKEN = "12345";

  public static WireMockServer wireMockServer;

  @Autowired
  private CustomerClient client;

  @BeforeAll
  static void wiremockStart() {
    wireMockServer = new WireMockServer(options()
        .port(8080));
    wireMockServer.start();
  }

  @Test
  void shouldReturnCustomer_ByEmail() {
    // Given
    Customer customer = new Customer("test@test.com", "test");
    String customerJson = new Gson().toJson(customer);
    wireMockServer.stubFor(
        WireMock.get(WireMock.urlPathTemplate("/api/customers/{email}"))
            .willReturn(WireMock.okJson(customerJson)));

    // When
    Customer responseCustomer = client.getCustomerByEmail(customer.getEmail(), TOKEN);

    // Then
    assertAll(
        () -> assertEquals(customer.getEmail(), responseCustomer.getEmail()),
        () -> assertEquals(customer.getPassword(), responseCustomer.getPassword())
    );
  }

  @Test
  void shouldNotReturnCustomer() {
    // Given
    Customer customer = new Customer("test@test.com", "test");
    wireMockServer.stubFor(
        WireMock.get(WireMock.urlPathTemplate("/api/customers/{email}"))
            .willReturn(WireMock.badRequest()));

    // When
    Executable executable = () -> client.getCustomerByEmail(customer.getEmail(), TOKEN);

    // Then
    assertThrows(FeignException.BadRequest.class, executable);
  }

  @Test
  void shouldReturnCustomer_AfterCreation() {
    // Given
    CustomerDto customerDto = new CustomerDto("test", "test@test.com", "test");
    String customerJson = new Gson().toJson(customerDto);
    wireMockServer.stubFor(
        WireMock.post(WireMock.urlPathMatching("/api/customers"))
            .withRequestBody(WireMock.equalToJson(customerJson))
            .willReturn(WireMock.okJson(customerJson)));

    // When
    CustomerDto responseCustomer = client.createCustomer(customerDto, TOKEN);

    // Then
    assertAll(
        () -> assertEquals(customerDto.getName(), responseCustomer.getName()),
        () -> assertEquals(customerDto.getEmail(), responseCustomer.getEmail()),
        () -> assertEquals(customerDto.getPassword(), responseCustomer.getPassword())
    );
  }

  @Test
  void shouldThrow_WhenCustomerExists() {
    // Given
    CustomerDto customerDto = new CustomerDto("test", "test@test.com", "test");
    String customerJson = new Gson().toJson(customerDto);
    wireMockServer.stubFor(
        WireMock.post(WireMock.urlPathMatching("/api/customers"))
            .withRequestBody(WireMock.equalToJson(customerJson))
            .willReturn(WireMock.badRequest()));

    // When
    Executable executable = () -> client.createCustomer(customerDto, TOKEN);

    // Then
    assertThrows(FeignException.class, executable);
  }

  @EnableFeignClients(clients = CustomerClient.class)
  @Configuration
  @EnableAutoConfiguration
  static class FeignConfig {

  }
}