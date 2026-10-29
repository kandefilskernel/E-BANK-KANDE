package com.ebank.customer.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ebank.customer.entities.Customer;
import com.ebank.customer.repositories.CustomerRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

	private final CustomerRepository customerRepository;

	public CustomerRestController(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@GetMapping
	public List<Customer> findAll() {
		return customerRepository.findAll();
	}

	@GetMapping("/{id}")
	public Customer findById(@PathVariable Long id) {
		return customerRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer " + id + " not found"));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Customer save(@Valid @RequestBody Customer customer) {
		customer.setId(null);
		return customerRepository.save(customer);
	}

	@PutMapping("/{id}")
	public Customer update(@PathVariable Long id, @Valid @RequestBody Customer customer) {
		findById(id);
		customer.setId(id);
		return customerRepository.save(customer);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		customerRepository.deleteById(id);
	}

}
