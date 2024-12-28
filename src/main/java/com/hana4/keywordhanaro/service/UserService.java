package com.hana4.keywordhanaro.service;

import com.hana4.keywordhanaro.exception.UserNotFoundException;
import com.hana4.keywordhanaro.model.dto.UserDto;

public interface UserService {
	Boolean checkMasterPassword(String id, String masterPassword) throws UserNotFoundException;
}
