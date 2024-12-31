package com.hana4.keywordhanaro.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hana4.keywordhanaro.model.entity.user.User;
import com.hana4.keywordhanaro.repository.UserRepository;
import com.hana4.keywordhanaro.utils.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findFirstByUsername(username)
			.orElseThrow(() -> new NullPointerException("User not Found"));

		return new CustomUserDetails(user);
	}
}
