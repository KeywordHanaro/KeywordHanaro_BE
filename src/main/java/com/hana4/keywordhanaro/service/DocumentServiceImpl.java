package com.hana4.keywordhanaro.service;

import org.springframework.stereotype.Service;

import com.hana4.keywordhanaro.exception.InvalidRequestException;
import com.hana4.keywordhanaro.model.dto.DocumentDto;
import com.hana4.keywordhanaro.model.mapper.DocumentMapper;
import com.hana4.keywordhanaro.repository.DocumentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

	private final DocumentRepository documentRepository;

	@Override
	public void addDocument(DocumentDto documentDto) {
		if (documentDto.getAmount() == null) {
			throw new InvalidRequestException("There is no amount in request");
		}
		switch (documentDto.getType()) {
			case DEPOSIT:
				if (documentDto.getSubAccount() == null) {
					throw new InvalidRequestException("There is no deposit target");
				}

				break;
			case WITHDRAW:
				if (documentDto.getAccount() == null) {
					throw new InvalidRequestException("There is no withdraw target");
				}

				break;
			case TRANSFER:
				if (documentDto.getAccount() == null && documentDto.getSubAccount() == null) {
					throw new InvalidRequestException("There is no transfer target");
				}
				break;
			default:
				throw new InvalidRequestException("Empty Document");
		}
		documentRepository.save(DocumentMapper.toEntity(documentDto));
	}
}
