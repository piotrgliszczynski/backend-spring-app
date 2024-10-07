package com.training.client;

import com.training.domain.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "data-service", url = "http://localhost:8080")
public interface CustomerClient {

  @GetMapping("/api/customers/{email}")
  Customer getCustomerByEmail(@PathVariable String email);
}
