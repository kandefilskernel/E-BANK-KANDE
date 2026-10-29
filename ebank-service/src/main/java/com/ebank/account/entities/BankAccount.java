package com.ebank.account.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ebank.account.enums.AccountType;
import com.ebank.account.model.Customer;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class BankAccount {

	@Id
	private String id;

	private BigDecimal balance;

	private String currency;

	@Enumerated(EnumType.STRING)
	private AccountType type;

	private LocalDate createdAt;

	private Long customerId;

	// Filled in from customer-service through OpenFeign, never stored in this database
	@Transient
	private Customer customer;

	public BankAccount() {
	}

	public BankAccount(String id, BigDecimal balance, String currency, AccountType type, LocalDate createdAt,
			Long customerId) {
		this.id = id;
		this.balance = balance;
		this.currency = currency;
		this.type = type;
		this.createdAt = createdAt;
		this.customerId = customerId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
