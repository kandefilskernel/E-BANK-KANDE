package com.ebank.account.model;

/**
 * Customer as exposed by customer-service. Not persisted here: only its id is
 * stored in a BankAccount.
 */
public record Customer(Long id, String name, String email) {
}
