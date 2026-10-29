package com.ebank.account.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ebank.account.entities.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

	List<BankAccount> findByCustomerId(Long customerId);

}
