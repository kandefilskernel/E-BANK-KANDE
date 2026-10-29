package com.ebank.customer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ebank.customer.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
