package com.training.client;

import com.training.domain.Customer;
import com.training.domain.dto.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "data-service", url = "http://localhost:8080")
public interface CustomerClient {

  @GetMapping("/api/customers/{email}")
  Customer getCustomerByEmail(@PathVariable String email);

  @PostMapping(value = "/api/customers", consumes = "application/json")
  CustomerDto createCustomer(@RequestBody CustomerDto customerDto);
}
