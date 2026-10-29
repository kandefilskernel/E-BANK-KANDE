package com.ebank.customer.mcp;

import java.util.List;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import com.ebank.customer.entities.Customer;
import com.ebank.customer.repositories.CustomerRepository;

/**
 * Customer operations exposed to AI agents through the MCP server (Streamable
 * HTTP, endpoint /mcp). The descriptions are what the LLM reads to decide which
 * tool to call, so they matter as much as the code.
 */
@Component
public class CustomerMcpTools {

	private final CustomerRepository customerRepository;

	public CustomerMcpTools(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@McpTool(name = "getAllCustomers", description = "List all the bank customers with their id, name and email")
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	@McpTool(name = "getCustomerById", description = "Get one bank customer (id, name, email) from its id")
	public Customer getCustomerById(@McpToolParam(description = "Id of the customer") Long id) {
		return customerRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Customer " + id + " not found"));
	}

	@McpTool(name = "searchCustomersByName", description = "Find bank customers whose name contains the given text, ignoring case")
	public List<Customer> searchCustomersByName(@McpToolParam(description = "Part of the customer name") String name) {
		return customerRepository.findByNameContainingIgnoreCase(name);
	}

}
