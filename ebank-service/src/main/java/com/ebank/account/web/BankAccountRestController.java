package com.ebank.account.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ebank.account.dtos.NewAccountRequest;
import com.ebank.account.dtos.TransferRequest;
import com.ebank.account.entities.BankAccount;
import com.ebank.account.services.BankAccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountRestController {

	private final BankAccountService bankAccountService;

	public BankAccountRestController(BankAccountService bankAccountService) {
		this.bankAccountService = bankAccountService;
	}

	@GetMapping
	public List<BankAccount> findAll() {
		return bankAccountService.findAll();
	}

	@GetMapping("/{id}")
	public BankAccount findById(@PathVariable String id) {
		return bankAccountService.findById(id);
	}

	@GetMapping("/customer/{customerId}")
	public List<BankAccount> findByCustomer(@PathVariable Long customerId) {
		return bankAccountService.findByCustomer(customerId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BankAccount create(@Valid @RequestBody NewAccountRequest request) {
		return bankAccountService.create(request);
	}

	@PostMapping("/transfer")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void transfer(@Valid @RequestBody TransferRequest request) {
		bankAccountService.transfer(request);
	}

}
