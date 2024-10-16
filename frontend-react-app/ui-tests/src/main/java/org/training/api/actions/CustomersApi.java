package org.training.api.actions;

import org.training.api.RestAssuredClient;
import org.training.data.Customer;
import org.training.exception.RestApiException;
import org.training.utils.config.TestConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomersApi {

  private final static String ENDPOINT_URL = TestConfig.customersApi;

  private final RestAssuredClient restClient;

  public CustomersApi() {
    this.restClient = new RestAssuredClient(TestConfig.apiBaseUrl);
  }

  public List<Customer> getCustomers() throws RestApiException {
    return Arrays.asList(
        restClient.get(ENDPOINT_URL).getBody().as(Customer[].class)
    );
  }

  public Customer createCustomer(Customer customer) throws RestApiException {
    return restClient.postJson(ENDPOINT_URL, customer).getBody().as(Customer.class);
  }

  public void deleteCustomer(Customer customer) throws RestApiException {
    Map<String, String> pathParams = new HashMap<>();
    pathParams.put("id", String.valueOf(customer.getId()));

    String url = ENDPOINT_URL + "/{id}";

    restClient.delete(url, pathParams);
  }
}
