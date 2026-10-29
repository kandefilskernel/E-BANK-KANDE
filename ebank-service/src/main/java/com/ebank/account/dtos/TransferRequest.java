package com.ebank.account.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransferRequest(
		@NotBlank String fromAccountId,
		@NotBlank String toAccountId,
		@NotNull @Positive BigDecimal amount) {
}
