package com.hana4.keywordhanaro.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hana4.keywordhanaro.exception.KeywordNotFoundException;
import com.hana4.keywordhanaro.exception.UserNotFoundException;

@RestControllerAdvice
public class ErrorController {

	private ResponseEntity<Map<String, Object>> createErrorResult(HttpStatus status, String message) {
		Map<String, Object> body = new HashMap<>();
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		return ResponseEntity.status(status).body(body);
	}

	@ExceptionHandler
	public ResponseEntity<Map<String, Object>> exceptionHandler(Exception e) {
		return createErrorResult(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<Map<String, Object>> nullPointerExceptionHandler(NullPointerException ne) {
		return createErrorResult(HttpStatus.NOT_FOUND, ne.getMessage());
	}

	@ExceptionHandler(KeywordNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleKeywordNotFound(KeywordNotFoundException e) {
		return createErrorResult(HttpStatus.NOT_FOUND, e.getMessage());
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleUserNotFound(KeywordNotFoundException e) {
		return createErrorResult(HttpStatus.NOT_FOUND, e.getMessage());
	}

}
