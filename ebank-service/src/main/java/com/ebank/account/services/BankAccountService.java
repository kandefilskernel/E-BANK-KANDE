package com.ebank.account.services;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ebank.account.clients.CustomerRestClient;
import com.ebank.account.dtos.NewAccountRequest;
import com.ebank.account.dtos.TransferRequest;
import com.ebank.account.entities.BankAccount;
import com.ebank.account.model.Customer;
import com.ebank.account.repositories.BankAccountRepository;

import feign.FeignException;

@Service
public class BankAccountService {

	private final BankAccountRepository bankAccountRepository;
	private final CustomerRestClient customerRestClient;

	public BankAccountService(BankAccountRepository bankAccountRepository, CustomerRestClient customerRestClient) {
		this.bankAccountRepository = bankAccountRepository;
		this.customerRestClient = customerRestClient;
	}

	public List<BankAccount> findAll() {
		List<BankAccount> accounts = bankAccountRepository.findAll();
		accounts.forEach(this::attachCustomer);
		return accounts;
	}

	public BankAccount findById(String id) {
		BankAccount account = getAccount(id);
		attachCustomer(account);
		return account;
	}

	public List<BankAccount> findByCustomer(Long customerId) {
		List<BankAccount> accounts = bankAccountRepository.findByCustomerId(customerId);
		accounts.forEach(this::attachCustomer);
		return accounts;
	}

	public BankAccount create(NewAccountRequest request) {
		BankAccount account = new BankAccount(UUID.randomUUID().toString(), request.initialBalance(),
				request.currency(), request.type(), LocalDate.now(), request.customerId());
		// Refuse to open an account for a customer that customer-service does not know
		Customer customer;
		try {
			customer = customerRestClient.findCustomerById(request.customerId());
		}
		catch (FeignException.NotFound e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Customer " + request.customerId() + " does not exist");
		}
		// Got the circuit breaker placeholder: the customer cannot be checked, so do not open the account
		if (customer.equals(Customer.unavailable(request.customerId()))) {
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
					"customer-service is unavailable, try again later");
		}
		// The id is assigned here, so save() merges and returns a new instance without the transient customer
		BankAccount saved = bankAccountRepository.save(account);
		saved.setCustomer(customer);
		return saved;
	}

	@Transactional
	public void transfer(TransferRequest request) {
		if (request.fromAccountId().equals(request.toAccountId())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Source and destination accounts are the same");
		}
		BankAccount from = getAccount(request.fromAccountId());
		BankAccount to = getAccount(request.toAccountId());
		if (!from.getCurrency().equals(to.getCurrency())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Accounts use different currencies");
		}
		if (from.getBalance().compareTo(request.amount()) < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance");
		}
		from.setBalance(from.getBalance().subtract(request.amount()));
		to.setBalance(to.getBalance().add(request.amount()));
	}

	private BankAccount getAccount(String id) {
		return bankAccountRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account " + id + " not found"));
	}

	private void attachCustomer(BankAccount account) {
		account.setCustomer(customerRestClient.findCustomerById(account.getCustomerId()));
	}

}
