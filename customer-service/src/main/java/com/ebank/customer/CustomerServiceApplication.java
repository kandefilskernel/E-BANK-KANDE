package com.ebank.customer;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ebank.customer.entities.Customer;
import com.ebank.customer.repositories.CustomerRepository;

@SpringBootApplication
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

	// The PostgreSQL database keeps its data between restarts: seed it only once, when it is empty
	@Bean
	CommandLineRunner seedCustomers(CustomerRepository customerRepository) {
		return args -> {
			if (customerRepository.count() > 0) {
				return;
			}
			customerRepository.saveAll(List.of(
					new Customer("Hassan", "hassan@gmail.com"),
					new Customer("Imane", "imane@gmail.com"),
					new Customer("Mohamed", "mohamed@gmail.com")));
		};
	}

}
