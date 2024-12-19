package com.hana4.keywordhanaro.service;

import org.springframework.stereotype.Service;

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
		documentRepository.save(DocumentMapper.toEntity(documentDto));
	}
}
