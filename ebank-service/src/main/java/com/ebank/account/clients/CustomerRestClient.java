package com.ebank.account.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ebank.account.model.Customer;

/**
 * Declarative REST client: "customer-service" is resolved through Eureka, so no
 * host or port is hard-coded here.
 */
@FeignClient(name = "customer-service")
public interface CustomerRestClient {

	@GetMapping("/api/customers/{id}")
	Customer findCustomerById(@PathVariable Long id);

	@GetMapping("/api/customers")
	List<Customer> allCustomers();

}
