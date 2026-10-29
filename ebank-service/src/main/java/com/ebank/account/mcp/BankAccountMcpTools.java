package com.ebank.account.mcp;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import com.ebank.account.dtos.NewAccountRequest;
import com.ebank.account.dtos.TransferRequest;
import com.ebank.account.entities.BankAccount;
import com.ebank.account.enums.AccountType;
import com.ebank.account.services.BankAccountService;

/**
 * Bank account operations exposed to AI agents through the MCP server
 * (Streamable HTTP, endpoint /mcp). They reuse BankAccountService, so the
 * agent gets the same business rules as the REST API.
 */
@Component
public class BankAccountMcpTools {

	private final BankAccountService bankAccountService;

	public BankAccountMcpTools(BankAccountService bankAccountService) {
		this.bankAccountService = bankAccountService;
	}

	@McpTool(name = "getAllAccounts", description = "List all the bank accounts with their id, type, balance, currency, creation date and owner customer")
	public List<BankAccount> getAllAccounts() {
		return bankAccountService.findAll();
	}

	@McpTool(name = "getAccountById", description = "Get one bank account (type, balance, currency, owner customer) from its id")
	public BankAccount getAccountById(@McpToolParam(description = "Id of the bank account (UUID)") String accountId) {
		return bankAccountService.findById(accountId);
	}

	@McpTool(name = "getAccountsByCustomer", description = "List the bank accounts owned by one customer")
	public List<BankAccount> getAccountsByCustomer(@McpToolParam(description = "Id of the customer") Long customerId) {
		return bankAccountService.findByCustomer(customerId);
	}

	@McpTool(name = "createAccount", description = "Open a new bank account for an existing customer")
	public BankAccount createAccount(
			@McpToolParam(description = "Id of the customer who owns the account") Long customerId,
			@McpToolParam(description = "Account type: CURRENT_ACCOUNT or SAVING_ACCOUNT") AccountType type,
			@McpToolParam(description = "Initial balance, zero or more") BigDecimal initialBalance,
			@McpToolParam(description = "Currency code, for example MAD") String currency) {
		return bankAccountService.create(new NewAccountRequest(customerId, type, initialBalance, currency));
	}

	@McpTool(name = "transferFunds", description = "Transfer an amount of money from one bank account to another. Both accounts must use the same currency and the source account must have enough balance")
	public String transferFunds(
			@McpToolParam(description = "Id of the account to debit") String fromAccountId,
			@McpToolParam(description = "Id of the account to credit") String toAccountId,
			@McpToolParam(description = "Amount to transfer, greater than zero") BigDecimal amount) {
		bankAccountService.transfer(new TransferRequest(fromAccountId, toAccountId, amount));
		BankAccount from = bankAccountService.findById(fromAccountId);
		BankAccount to = bankAccountService.findById(toAccountId);
		return "Transferred %s %s from %s (new balance %s) to %s (new balance %s)".formatted(amount,
				from.getCurrency(), fromAccountId, from.getBalance(), toAccountId, to.getBalance());
	}

}
