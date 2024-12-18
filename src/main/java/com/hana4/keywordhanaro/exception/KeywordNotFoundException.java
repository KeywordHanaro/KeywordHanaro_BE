package com.hana4.keywordhanaro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class KeywordNotFoundException extends RuntimeException {
	public KeywordNotFoundException(String message) {
		super(message);
	}

	public KeywordNotFoundException() {
		super("Keyword not found");
	}

}
