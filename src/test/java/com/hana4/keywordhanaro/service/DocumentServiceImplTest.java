package com.hana4.keywordhanaro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.internal.verification.VerificationModeFactory.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.hana4.keywordhanaro.exception.InvalidRequestException;
import com.hana4.keywordhanaro.model.dto.AccountDto;
import com.hana4.keywordhanaro.model.dto.DocumentDto;
import com.hana4.keywordhanaro.model.entity.document.DocumentType;
import com.hana4.keywordhanaro.repository.DocumentRepository;

@SpringBootTest
class DocumentServiceImplTest {

	@MockBean
	private DocumentRepository documentRepository;

	@Autowired
	private DocumentServiceImpl documentService;

	@Test
	void documentTest() {
		DocumentDto documentDto = new DocumentDto(1L, DocumentType.DEPOSIT, new AccountDto(), new AccountDto(),
			new BigDecimal(100));

		documentDto.setAmount(null);

		assertThrows(InvalidRequestException.class, () -> {documentService.addDocument(documentDto);});

		documentDto.setAmount(new BigDecimal(10000));
		documentDto.setAccount(null);
		documentDto.setSubAccount(null);
		assertThrows(InvalidRequestException.class, () -> {documentService.addDocument(documentDto);});

		DocumentDto documentDto2 = new DocumentDto(1L, DocumentType.WITHDRAW, new AccountDto(), new AccountDto(),
			null);
		assertThrows(InvalidRequestException.class, () -> {documentService.addDocument(documentDto2);});
		documentDto2.setAmount(new BigDecimal(10000));
		documentDto2.setAccount(null);
		assertThrows(InvalidRequestException.class, () -> {documentService.addDocument(documentDto2);});
	}
}
