package com.hana4.keywordhanaro.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.model.dto.UserDto;
import com.hana4.keywordhanaro.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LoginController {
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserDto userDto) throws Exception {
		try {
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
			);
		} catch (BadCredentialsException e) {
			throw new NullPointerException("Incorrect username or password");
		}

		final UserDetails userDetails = userDetailsService
			.loadUserByUsername(userDto.getUsername());

		final String jwt = jwtUtil.createJwt(userDetails.getUsername());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + jwt);

		return ResponseEntity.ok().headers(headers).body("Login Successful");
	}
}
