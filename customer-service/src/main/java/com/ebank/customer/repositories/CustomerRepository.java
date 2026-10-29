package com.ebank.customer.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ebank.customer.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	List<Customer> findByNameContainingIgnoreCase(String name);

}
