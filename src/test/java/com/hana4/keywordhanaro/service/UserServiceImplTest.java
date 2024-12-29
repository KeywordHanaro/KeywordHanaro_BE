package com.hana4.keywordhanaro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.hana4.keywordhanaro.exception.InvalidRequestException;
import com.hana4.keywordhanaro.exception.UserNotFoundException;
import com.hana4.keywordhanaro.model.entity.user.User;
import com.hana4.keywordhanaro.repository.UserRepository;

@SpringBootTest
class UserServiceImplTest {
	@MockBean
	private UserRepository userRepository;

	@MockBean
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserService userService;

	@Test
	void testCheckMasterPassword_UserNotFound() {
		// given
		// when(userRepository.findById(anyString()))
		// 	.thenThrow(new UserNotFoundException("cannot find user by id"));

		// when, then
		assertThrows(UserNotFoundException.class,
			() -> userService.checkMasterPassword("testId", "password"));
	}

	@Test
	void testCheckMasterPassword_NullMasterPassword() {
		// given
		User user = new User();
		user.setMasterPassword(null);
		when(userRepository.findById("testId"))
			.thenThrow(new InvalidRequestException("MasterPassword is null"));

		// when, then
		assertThrows(InvalidRequestException.class,
			() -> userService.checkMasterPassword("testId", "password"));
	}

	@Test
	void testCheckMasterPassword_ValidPassword() throws UserNotFoundException {
		// given
		User user = new User();
		user.setMasterPassword("encodedPassword");
		when(userRepository.findById("testId"))
			.thenReturn(Optional.of(user));
		when(bCryptPasswordEncoder.matches("password", "encodedPassword"))
			.thenReturn(true);

		// when
		Boolean result = userService.checkMasterPassword("testId", "password");

		// then
		assertTrue(result);
	}

	@Test
	void testCheckMasterPassword_InvalidPassword() throws UserNotFoundException {
		// given
		User user = new User();
		user.setMasterPassword("encodedPassword");
		when(userRepository.findById("testId"))
			.thenReturn(Optional.of(user));
		when(bCryptPasswordEncoder.matches("wrongPassword", "encodedPassword"))
			.thenReturn(false);

		// when
		Boolean result = userService.checkMasterPassword("testId", "wrongPassword");

		// then
		assertFalse(result);
	}
}
