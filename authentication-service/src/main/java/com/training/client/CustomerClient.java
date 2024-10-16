package com.training.client;

import com.training.domain.Customer;
import com.training.domain.dto.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(
    name = "data-service",
    url = "${spring.cloud.openfeign.client.config.data-service.url}")
public interface CustomerClient {

  @GetMapping("/api/customers/{email}")
  Customer getCustomerByEmail(@PathVariable String email,
                              @RequestHeader("Authorization") String token);

  @PostMapping(value = "/api/customers", consumes = "application/json")
  CustomerDto createCustomer(@RequestBody CustomerDto customerDto,
                             @RequestHeader("Authorization") String token);
}

