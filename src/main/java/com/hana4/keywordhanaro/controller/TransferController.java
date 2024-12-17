package com.hana4.keywordhanaro.controller;

import com.hana4.keywordhanaro.model.dto.TransactionDTO;
import com.hana4.keywordhanaro.model.dto.TransferRequestDTO;
import com.hana4.keywordhanaro.model.entity.transaction.Transaction;
import com.hana4.keywordhanaro.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/tranfer")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransactionDTO> transfer(@RequestBody TransferRequestDTO transferRequestDTO) {
            Transaction transaction = transferService.transfer(
                    transferRequestDTO.getFromAccountNumber(),
                    transferRequestDTO.getToAccountNumber(),
                    transferRequestDTO.getAmount()
            );
            TransactionDTO responseDTO = TransactionDTO.builder()
                    .fromAccountId(String.valueOf(transaction.getAccount()))
                    .toAccountId(String.valueOf(transaction.getSubAccount()))
                    .amount(transaction.getAmount())
                    .status("SUCCESS")
                    .beforeBalance(transaction.getAccount().getBalance())
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Transaction-Time", LocalDateTime.now().toString());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(headers)
                    .body(responseDTO);


    }
}


