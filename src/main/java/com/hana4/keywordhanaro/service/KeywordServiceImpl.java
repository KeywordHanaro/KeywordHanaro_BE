package com.hana4.keywordhanaro.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hana4.keywordhanaro.exception.InvalidRequestException;
import com.hana4.keywordhanaro.model.dto.KeywordDto;
import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.keyword.Keyword;
import com.hana4.keywordhanaro.model.entity.keyword.KeywordType;
import com.hana4.keywordhanaro.model.entity.user.User;
import com.hana4.keywordhanaro.model.mapper.KeywordMapper;
import com.hana4.keywordhanaro.repository.AccountRepository;
import com.hana4.keywordhanaro.repository.KeywordRepository;
import com.hana4.keywordhanaro.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KeywordServiceImpl implements KeywordService {

	private final KeywordRepository keywordRepository;
	private final UserRepository userRepository;
	private final AccountRepository accountRepository;

	private static final Long SEQ_ORDER_INTERVAL = 100L;

	@Override
	public KeywordDto createKeyword(KeywordDto keywordDto) {
		User user = userRepository.findById(keywordDto.getUserId())
			.orElseThrow(() -> new NullPointerException("User not found"));

		// !!!!!!!!!!!!
		Account account = accountRepository.findByAccountNumber(keywordDto.getAccountId());
		// .orElseThrow(() -> new NullPointerException("Account not found"));
		Account subAccount = accountRepository.findByAccountNumber(keywordDto.getSubAccountId());

		// 리스트 순서
		Long newSeqOrder = keywordRepository.findTopByUserIdOrderBySeqOrderDesc(keywordDto.getUserId())
			.map(keyword -> keyword.getSeqOrder() + SEQ_ORDER_INTERVAL)
			.orElse(SEQ_ORDER_INTERVAL);

		Keyword keyword;

		switch (keywordDto.getType()) {
			case "INQUIRY":
				keyword = new Keyword(user, KeywordType.INQUIRY, keywordDto.getName(), keywordDto.getDesc(),
					newSeqOrder, account, keywordDto.getInquiryWord());
				break;

			case "TRANSFER":
				keyword = new Keyword(user, KeywordType.TRANSFER, keywordDto.getName(), keywordDto.getDesc(),
					newSeqOrder, account, subAccount, keywordDto.getAmount(), keywordDto.getCheckEveryTime());
				break;

			case "TICKET":
				keyword = new Keyword(user, KeywordType.TICKET, keywordDto.getName(), keywordDto.getDesc(),
					newSeqOrder, keywordDto.getBranch());
				break;

			case "SETTLEMENT":
				keyword = new Keyword(user, KeywordType.SETTLEMENT, keywordDto.getName(), keywordDto.getDesc(),
					newSeqOrder, account, keywordDto.getGroupMember(), keywordDto.getAmount(),
					keywordDto.getCheckEveryTime());
				break;

			default:
				throw new InvalidRequestException("Invalid keyword type");
		}

		keyword = keywordRepository.save(keyword);
		return KeywordMapper.toDto(keyword);
	}

	@Override
	public ResponseEntity<KeywordMapper.DeleteResponse> removeKeyword(Long id) {
		Optional<Keyword> keyword = keywordRepository.findById(id);
		if (keyword.isPresent()) {
			keywordRepository.delete(keyword.get());
			return ResponseEntity.ok(new KeywordMapper.DeleteResponse(true, "Keyword deleted successfully"));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new KeywordMapper.DeleteResponse(false, "Keyword not found"));
		}
	}
}
