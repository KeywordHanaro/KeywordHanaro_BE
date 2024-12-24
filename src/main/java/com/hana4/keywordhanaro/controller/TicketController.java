package com.hana4.keywordhanaro.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.exception.UserNotFoundException;
import com.hana4.keywordhanaro.model.dto.TicketDto;
import com.hana4.keywordhanaro.model.dto.TicketRequestDto;
import com.hana4.keywordhanaro.model.dto.UserDto;
import com.hana4.keywordhanaro.model.mapper.UserMapper;
import com.hana4.keywordhanaro.service.TicketService;
import com.hana4.keywordhanaro.utils.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket")
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor
public class TicketController {
	private final TicketService ticketService;
	private final UserDetailsService userDetailsService;

	@Operation(
		summary = "번호표 생성",
		description = "지점별 번호표를 생성합니다"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "번호표가 성공적으로 생성되었습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 (필수 필드 누락, 잘못된 데이터 등)", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "404", description = "사용자 또는 키워드를 찾을 수 없음", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
	})
	@PostMapping()
	public ResponseEntity<TicketDto> createTicket(@RequestBody TicketRequestDto ticketRequestDto) throws Exception {
		return ResponseEntity.ok(ticketService.createTicket(ticketRequestDto));
	}

	@Operation(
		summary = "서비스 이용 동의",
		description = "위치 기반 서비스 이용 동의를 사용자 정보에 저장합니다"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "서비스 이용 동의가 저장되었습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
	})
	@PostMapping("/permission")
	public ResponseEntity<String> updatePermission(@RequestBody Short location, Authentication authentication) throws
		UserNotFoundException {
		CustomUserDetails userDetails = (CustomUserDetails)userDetailsService.loadUserByUsername(
			authentication.getName());
		UserDto userDto = UserMapper.toDto(userDetails.getUser());
		ticketService.updatePermission(location, userDto);
		return ResponseEntity.ok("Permission updated successfully");
	}
}
