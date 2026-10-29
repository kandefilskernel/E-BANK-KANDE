package com.ebank.chatbot.agents;

import java.util.Arrays;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

/**
 * The AI agent: an LLM that answers banking questions by calling the MCP tools
 * of customer-service and ebank-service. Each conversation (one per Telegram
 * chat, for example) keeps its own short-term memory.
 */
@Service
public class BankingAgent {

	private static final String SYSTEM_PROMPT = """
			You are the assistant of E-BANK, a bank. You help bank employees consult customers and bank accounts
			and run banking operations, using only the tools you are given.
			Rules:
			- Always use the tools to get data; never invent customers, accounts, ids or balances.
			- To find a customer by name, search it first, then use its id.
			- Before calling transferFunds or createAccount, summarize the operation (accounts, owner names, amount)
			  and ask the user to confirm, unless the user's last message already confirms that exact operation.
			- If a tool returns an error, explain it simply to the user.
			- Answer in the language of the user, briefly. Present lists as short lines, with amounts and currency.
			""";

	private final ChatClient chatClient;
	private final ToolCallbackProvider mcpTools;

	public BankingAgent(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, ToolCallbackProvider mcpTools) {
		this.mcpTools = mcpTools;
		this.chatClient = chatClientBuilder
				.defaultSystem(SYSTEM_PROMPT)
				.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
				.defaultToolCallbacks(mcpTools)
				.build();
	}

	public String ask(String conversationId, String question) {
		return chatClient.prompt()
				.user(question)
				.advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
				.call()
				.content();
	}

	/** Names of the MCP tools the agent can call, discovered from the MCP servers. */
	public List<String> toolNames() {
		return Arrays.stream(mcpTools.getToolCallbacks())
				.map(toolCallback -> toolCallback.getToolDefinition().name())
				.toList();
	}

}
