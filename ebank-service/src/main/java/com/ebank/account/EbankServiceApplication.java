package com.ebank.account;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import com.ebank.account.entities.BankAccount;
import com.ebank.account.enums.AccountType;
import com.ebank.account.repositories.BankAccountRepository;

@SpringBootApplication
@EnableFeignClients
public class EbankServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankServiceApplication.class, args);
	}

	// Two accounts for each customer seeded by customer-service (ids 1 to 3), only when the database is empty
	@Bean
	CommandLineRunner seedAccounts(BankAccountRepository bankAccountRepository) {
		return args -> {
			if (bankAccountRepository.count() > 0) {
				return;
			}
			for (long customerId = 1; customerId <= 3; customerId++) {
				for (AccountType type : AccountType.values()) {
					bankAccountRepository.save(new BankAccount(UUID.randomUUID().toString(),
							BigDecimal.valueOf(1000 + Math.random() * 90000).setScale(2, RoundingMode.HALF_UP),
							"MAD", type, LocalDate.now(), customerId));
				}
			}
		};
	}

}
