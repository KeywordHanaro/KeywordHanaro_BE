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

		Account account = null;
		Account subAccount = null;

		// 리스트 순서
		Long newSeqOrder = keywordRepository.findTopByUserIdOrderBySeqOrderDesc(keywordDto.getUserId())
			.map(keyword -> keyword.getSeqOrder() + SEQ_ORDER_INTERVAL)
			.orElse(SEQ_ORDER_INTERVAL);

		Keyword keyword;

		switch (keywordDto.getType()) {
			case "INQUIRY":
				account = getAccount(keywordDto.getAccount().getId());
				keyword = new Keyword(user, KeywordType.INQUIRY, keywordDto.getName(), keywordDto.getDesc(),
					newSeqOrder, account, keywordDto.getInquiryWord());
				break;

			case "TRANSFER":
				account = getAccount(keywordDto.getAccount().getId());
				subAccount = getSubAccount(keywordDto.getSubAccount().getAccountNumber());
				keyword = new Keyword(user, KeywordType.TRANSFER, keywordDto.getName(), keywordDto.getDesc(),
					newSeqOrder, account, subAccount, keywordDto.getAmount(), keywordDto.getCheckEveryTime());
				break;

			case "TICKET":
				keyword = new Keyword(user, KeywordType.TICKET, keywordDto.getName(), keywordDto.getDesc(),
					newSeqOrder, keywordDto.getBranch());
				break;

			case "SETTLEMENT":
				account = getAccount(keywordDto.getAccount().getId());
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

	private Account getAccount(Long accountId) {
		return accountRepository.findById(accountId)
			.orElseThrow(() -> new NullPointerException("Withdrawal Account not found"));
	}

	private Account getSubAccount(String accountNumber) {
		return accountRepository.findByAccountNumber(accountNumber)
			.orElseThrow(() -> new NullPointerException("Receiving account not found"));
	}

	@Override
	public KeywordDto updateKeyword(Long id, KeywordDto keywordDto){
		Keyword existingKeyword = keywordRepository.findById(id).orElseThrow(() -> new NullPointerException("Keyword not found"));

		// 기본 정보 업데이트
		existingKeyword.setName(keywordDto.getName());
		existingKeyword.setDescription(keywordDto.getDesc());
		existingKeyword.setFavorite(keywordDto.getIsFavorite());

		// 계좌 정보 업데이트
		if (keywordDto.getAccount() != null) {
			Account account = accountRepository.findById(keywordDto.getAccount().getId()).orElseThrow(() -> new NullPointerException("Account not found"));
			existingKeyword.setAccount(account);
		}

		if (keywordDto.getSubAccount() != null) {
			Account subAccount = accountRepository.findByAccountNumber(keywordDto.getSubAccount().getAccountNumber()).orElseThrow(() -> new NullPointerException("Account not found"));
			existingKeyword.setSubAccount(subAccount);
		}

		// 타입별 특정 필드 업데이트
		switch (existingKeyword.getType()) {
			case INQUIRY:
				existingKeyword.setInquiryWord(keywordDto.getInquiryWord());
				break;
			case TRANSFER:
				existingKeyword.setAmount(keywordDto.getAmount());
				existingKeyword.setCheckEveryTime(keywordDto.getCheckEveryTime());
				break;
			case TICKET:
				existingKeyword.setBranch(keywordDto.getBranch());
				break;
			case SETTLEMENT:
				existingKeyword.setGroupMember(keywordDto.getGroupMember());
				existingKeyword.setAmount(keywordDto.getAmount());
				existingKeyword.setCheckEveryTime(keywordDto.getCheckEveryTime());
				break;
		}

		Keyword updatedKeyword = keywordRepository.save(existingKeyword);
		return KeywordMapper.toDto(updatedKeyword);
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
