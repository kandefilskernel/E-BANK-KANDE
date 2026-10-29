package com.ebank.account.model;

/**
 * Customer as exposed by customer-service. Not persisted here: only its id is
 * stored in a BankAccount.
 */
public record Customer(Long id, String name, String email) {

	public static final String NOT_AVAILABLE = "Not available";

	/** Placeholder returned by the circuit breaker fallback when customer-service is down. */
	public static Customer unavailable(Long id) {
		return new Customer(id, NOT_AVAILABLE, NOT_AVAILABLE);
	}

}
