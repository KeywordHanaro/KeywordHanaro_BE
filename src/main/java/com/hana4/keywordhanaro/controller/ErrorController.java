package com.hana4.keywordhanaro.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.hana4.keywordhanaro.exception.AccountNotFoundException;
import com.hana4.keywordhanaro.exception.InvalidRequestException;
import com.hana4.keywordhanaro.exception.KakaoApiException;
import com.hana4.keywordhanaro.exception.KeywordNotFoundException;
import com.hana4.keywordhanaro.exception.TicketNotFoundException;
import com.hana4.keywordhanaro.exception.TransferErrorException;
import com.hana4.keywordhanaro.exception.UnAuthorizedException;
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
		return createErrorResult(HttpStatus.INTERNAL_SERVER_ERROR, ne.getMessage());
	}

	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<Map<String, Object>> invalidRequestExceptionHandler(InvalidRequestException ire) {
		return createErrorResult(HttpStatus.BAD_REQUEST, ire.getMessage());
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<Map<String, Object>> noHandlerFoundExceptionHandler(
		NoHandlerFoundException ex) {
		return createErrorResult(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<Map<String, Object>> accountNotFoundExceptionHandler(AccountNotFoundException ae) {
		return createErrorResult(HttpStatus.NOT_FOUND, ae.getMessage());
	}

	@ExceptionHandler(KakaoApiException.class)
	public ResponseEntity<Map<String, Object>> kakaoApiExceptionHandler(KakaoApiException ae) {
		return createErrorResult(HttpStatus.INTERNAL_SERVER_ERROR, ae.getMessage());
	}

	@ExceptionHandler(KeywordNotFoundException.class)
	public ResponseEntity<Map<String, Object>> keywordNotFoundExceptionHandler(KeywordNotFoundException ke) {
		return createErrorResult(HttpStatus.NOT_FOUND, ke.getMessage());
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Map<String, Object>> userNotFoundExceptionHandler(UserNotFoundException ue) {
		return createErrorResult(HttpStatus.NOT_FOUND, ue.getMessage());
	}

	@ExceptionHandler(UnAuthorizedException.class)
	public ResponseEntity<Map<String, Object>> unAuthorizedExceptionHandler(UnAuthorizedException ue) {
		return createErrorResult(HttpStatus.UNAUTHORIZED, ue.getMessage());
	}

	@ExceptionHandler(TransferErrorException.class)
	public ResponseEntity<Map<String, Object>> transferErrorExceptionHandler(TransferErrorException te) {
		return createErrorResult(HttpStatus.BAD_REQUEST, te.getMessage());
	}

	@ExceptionHandler(TicketNotFoundException.class)
	public ResponseEntity<Map<String, Object>> ticketNotFoundExceptionHandler(TicketNotFoundException te) {
		return createErrorResult(HttpStatus.NOT_FOUND, te.getMessage());
	}
}
