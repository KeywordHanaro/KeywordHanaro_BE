package com.hana4.keywordhanaro.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.model.dto.AccountDto;
import com.hana4.keywordhanaro.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

	private final AccountService accountService;

	@GetMapping("/{id}")
	public AccountDto getAccount(@PathVariable("id") Long id) {
		return accountService.getAccount(id);
	}

	@GetMapping
	public List<AccountDto> getAccounts() {
		return accountService.getAccounts();
	}
}
