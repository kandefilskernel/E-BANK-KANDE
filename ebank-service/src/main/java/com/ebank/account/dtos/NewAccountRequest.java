package com.ebank.account.dtos;

import java.math.BigDecimal;

import com.ebank.account.enums.AccountType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record NewAccountRequest(
		@NotNull Long customerId,
		@NotNull AccountType type,
		@NotNull @PositiveOrZero BigDecimal initialBalance,
		@NotBlank String currency) {
}
