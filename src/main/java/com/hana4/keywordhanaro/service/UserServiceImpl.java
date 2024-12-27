package com.hana4.keywordhanaro.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hana4.keywordhanaro.exception.InvalidRequestException;
import com.hana4.keywordhanaro.exception.UserNotFoundException;
import com.hana4.keywordhanaro.model.entity.user.User;
import com.hana4.keywordhanaro.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public Boolean checkMasterPassword(String username, String masterPassword) throws UserNotFoundException {
		User user = userRepository.findFirstByUsername(username)
			.orElseThrow(() -> new UserNotFoundException("cannot find user"));
		if (user.getMasterPassword() == null) {
			throw new InvalidRequestException("MasterPassword is null");
		}
		return bCryptPasswordEncoder.matches(masterPassword, user.getMasterPassword());
	}
}
