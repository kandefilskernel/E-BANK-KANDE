package com.ebank.account.clients;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import com.ebank.account.model.Customer;

import feign.FeignException;

/**
 * Called by the circuit breaker when a call to customer-service fails or when
 * the circuit is open: bank accounts stay readable with placeholder customer
 * data instead of failing with a 500.
 */
@Component
public class CustomerRestClientFallbackFactory implements FallbackFactory<CustomerRestClient> {

	private static final Logger log = LoggerFactory.getLogger(CustomerRestClientFallbackFactory.class);

	@Override
	public CustomerRestClient create(Throwable cause) {
		return new CustomerRestClient() {

			@Override
			public Customer findCustomerById(Long id) {
				// An unknown customer is a business answer, not an outage: let the caller see it
				if (cause instanceof FeignException.NotFound notFound) {
					throw notFound;
				}
				log.warn("customer-service unavailable, fallback for customer {}: {}", id, cause.toString());
				return Customer.unavailable(id);
			}

			@Override
			public List<Customer> allCustomers() {
				log.warn("customer-service unavailable, fallback for all customers: {}", cause.toString());
				return List.of();
			}

		};
	}

}
