package com.hana4.keywordhanaro.controller;

import com.hana4.keywordhanaro.exception.AccountNotFoundException;
import com.hana4.keywordhanaro.model.dto.TransactionDto;
import com.hana4.keywordhanaro.model.dto.TransferRequestDto;
import com.hana4.keywordhanaro.model.entity.transaction.Transaction;
import com.hana4.keywordhanaro.model.mapper.AccountMapper;
import com.hana4.keywordhanaro.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transfer")
@Tag(name = "transfer-controller", description = "이체에 대한 송금자, 수취자, 금액 정보를 처리")
public class TransferController {
    private final TransferService transferService;

    @Operation(summary = "송금 요청", description = "송금자 계좌번호, 수취자 계좌번호, 송금 금액을 받아 이체를 수행")
    @Parameters({
            @Parameter(
                    name = "TransferRequestDto",
                    description = "송금 요청 정보 (송금자, 수취자, 금액)",
                    required = true,
                    schema = @Schema(implementation = TransferRequestDto.class)
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "송금 요청이 성공적으로 처리되었습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 파라미터입니다.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류가 발생했습니다.",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping
    public ResponseEntity<TransactionDto> transfer(@RequestBody @Parameter(description = "송금 요청 데이터", required = true) TransferRequestDto transferRequestDto) throws
            AccountNotFoundException {
        Transaction transaction = transferService.transfer(
                transferRequestDto.getFromAccountNumber(),
                transferRequestDto.getToAccountNumber(),
                transferRequestDto.getAmount()
        );
        TransactionDto responseDTO = TransactionDto.builder()
                .account(AccountMapper.toDto(transaction.getAccount()))
                .subAccount(AccountMapper.toDto(transaction.getSubAccount()))
                .amount(transaction.getAmount())
                .status("SUCCESS")
                .beforeBalance(transaction.getAccount().getBalance())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDTO);
    }
}