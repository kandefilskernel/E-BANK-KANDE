package com.ebank.chatbot.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebank.chatbot.agents.BankingAgent;

/** HTTP access to the agent, used to test it without Telegram and later by the web front-end. */
@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

	private final BankingAgent bankingAgent;

	public ChatRestController(BankingAgent bankingAgent) {
		this.bankingAgent = bankingAgent;
	}

	@GetMapping
	public String chat(@RequestParam String query, @RequestParam(defaultValue = "web") String conversationId) {
		return bankingAgent.ask(conversationId, query);
	}

	@GetMapping("/tools")
	public List<String> tools() {
		return bankingAgent.toolNames();
	}

}
