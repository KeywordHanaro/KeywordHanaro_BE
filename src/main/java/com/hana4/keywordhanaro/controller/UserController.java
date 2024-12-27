package com.hana4.keywordhanaro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.exception.UserNotFoundException;
import com.hana4.keywordhanaro.model.dto.UserDto;
import com.hana4.keywordhanaro.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	private final UserService userService;

	@GetMapping("/checkMasterPassword")
	public ResponseEntity<Boolean> checkMasterPassword(@RequestBody UserDto userDto, Authentication authentication) throws
		UserNotFoundException {
		return ResponseEntity.ok(userService.checkMasterPassword(authentication.getName(), userDto.getMasterPassword()));
	}
}
