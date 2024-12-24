package com.hana4.keywordhanaro.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.exception.UnAuthorizedException;
import com.hana4.keywordhanaro.model.dto.UserDto;
import com.hana4.keywordhanaro.utils.CustomUserDetails;
import com.hana4.keywordhanaro.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LoginController {
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody UserDto userDto) throws Exception {
		try {
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
			);
		} catch (BadCredentialsException e) {
			throw new UnAuthorizedException("Incorrect username or password");
		}

		final CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService
			.loadUserByUsername(userDto.getUsername());

		final String jwt = jwtUtil.createJwt(customUserDetails.getUsername());


		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + jwt);

		Map<String, Object> response = new HashMap<>();
		response.put("permission", customUserDetails.getUser().getPermission());
		return ResponseEntity.ok().headers(headers).body(response);
	}
}
