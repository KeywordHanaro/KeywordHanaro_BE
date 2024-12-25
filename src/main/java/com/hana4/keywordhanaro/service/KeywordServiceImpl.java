package com.hana4.keywordhanaro.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana4.keywordhanaro.exception.AccountNotFoundException;
import com.hana4.keywordhanaro.exception.InvalidRequestException;
import com.hana4.keywordhanaro.exception.KeywordNotFoundException;
import com.hana4.keywordhanaro.model.dto.BranchDto;
import com.hana4.keywordhanaro.model.dto.DeleteResponseDto;
import com.hana4.keywordhanaro.model.dto.KeywordDto;
import com.hana4.keywordhanaro.model.dto.KeywordResponseDto;
import com.hana4.keywordhanaro.model.dto.MultiKeywordDto;
import com.hana4.keywordhanaro.model.dto.TransactionDto;
import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.keyword.Keyword;
import com.hana4.keywordhanaro.model.entity.keyword.KeywordType;
import com.hana4.keywordhanaro.model.entity.keyword.MultiKeyword;
import com.hana4.keywordhanaro.model.entity.user.User;
import com.hana4.keywordhanaro.model.mapper.KeywordMapper;
import com.hana4.keywordhanaro.model.mapper.KeywordResponseMapper;
import com.hana4.keywordhanaro.model.mapper.UserResponseMapper;
import com.hana4.keywordhanaro.repository.AccountRepository;
import com.hana4.keywordhanaro.repository.KeywordRepository;
import com.hana4.keywordhanaro.repository.MultiKeywordRepository;
import com.hana4.keywordhanaro.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeywordServiceImpl implements KeywordService {

	private final KeywordRepository keywordRepository;
	private final UserRepository userRepository;
	private final AccountRepository accountRepository;
	private final InquiryService inquiryService;
	private final MultiKeywordRepository multiKeywordRepository;
	private final ObjectMapper objectMapper;

	private static final Long SEQ_ORDER_INTERVAL = 100L;

	@Override
	@Transactional
	public KeywordDto createKeyword(KeywordDto keywordDto) throws Exception {
		User user = UserResponseMapper.toEntity(keywordDto.getUser());

		Account account = null;
		Account subAccount = null;

		validateCommonRequest(keywordDto);

		// 리스트 순서
		Long newSeqOrder = keywordRepository.findTopByUserIdOrderBySeqOrderDesc(keywordDto.getUser().getId())
			.map(keyword -> keyword.getSeqOrder() + SEQ_ORDER_INTERVAL)
			.orElse(SEQ_ORDER_INTERVAL);

		Keyword keyword;

		switch (keywordDto.getType()) {
			case "INQUIRY":
				validateInquiryKeyword(keywordDto);
				account = getAccount(keywordDto.getAccount().getId());
				keyword = new Keyword(user, KeywordType.INQUIRY, keywordDto.getName(), keywordDto.getDesc(),
					newSeqOrder, account, keywordDto.getInquiryWord());
				break;

			case "TRANSFER":
				validateTransferKeyword(keywordDto);
				account = getAccount(keywordDto.getAccount().getId());
				subAccount = getSubAccount(keywordDto.getSubAccount().getAccountNumber());
				keyword = new Keyword(user, KeywordType.TRANSFER, keywordDto.getName(), keywordDto.getDesc(),
					newSeqOrder, account, subAccount, keywordDto.getAmount(), keywordDto.getCheckEveryTime());
				break;

			case "TICKET":
				validateTicketKeyword(keywordDto);
				keyword = new Keyword(user, KeywordType.TICKET, keywordDto.getName(), keywordDto.getDesc(),
					newSeqOrder, keywordDto.getBranch());
				break;

			case "SETTLEMENT":
				validateSettlementKeyword(keywordDto);
				account = getAccount(keywordDto.getAccount().getId());
				keyword = new Keyword(user, KeywordType.SETTLEMENT, keywordDto.getName(), keywordDto.getDesc(),
					newSeqOrder, account, keywordDto.getGroupMember(), keywordDto.getAmount(),
					keywordDto.getCheckEveryTime());
				break;

			case "MULTI":
				validateMultiKeyword(keywordDto);
				keyword = new Keyword(user, KeywordType.MULTI, keywordDto.getName(), keywordDto.getDesc(), newSeqOrder);
				break;

			default:
				throw new InvalidRequestException("Invalid keyword type");
		}

		keyword = keywordRepository.save(keyword);

		// System.out.println("keyword = " + keyword);
		// System.out.println("Saved Keyword ID: " + keyword.getId());

		// MULTI 키워드 처리
		if (keywordDto.getType().equals("MULTI") && keywordDto.getMultiKeyword() != null) {
			for (MultiKeywordDto multiKeywordDto : keywordDto.getMultiKeyword()) {
				Keyword subKeyword = keywordRepository.findById(multiKeywordDto.getKeyword().getId())
					.orElseThrow(() -> new KeywordNotFoundException(
						"Keyword not found with id: " + multiKeywordDto.getKeyword().getId()));

				MultiKeyword multiKeyword = new MultiKeyword();
				multiKeyword.setMultiKeyword(keyword);
				multiKeyword.setKeyword(subKeyword);
				multiKeyword.setSeqOrder(multiKeywordDto.getSeqOrder());

				keyword.addMultiKeyword(multiKeyword); // 중요
			}

			keyword = keywordRepository.save(keyword);
		}

		// System.out.println("Final Keyword: " + keyword);
		// System.out.println("Number of MultiKeywords: " + (keyword.getMultiKeywords() != null ? keyword.getMultiKeywords().size() : 0));

		return KeywordMapper.toDto(keyword);
	}

	private Account getAccount(Long accountId) {
		return accountRepository.findById(accountId)
			.orElseThrow(() -> new NullPointerException("Account not found"));
	}

	private Account getSubAccount(String accountNumber) {
		return accountRepository.findByAccountNumber(accountNumber)
			.orElseThrow(() -> new NullPointerException("Receiving account not found"));
	}

	@Override
	public KeywordDto updateKeyword(Long id, KeywordDto keywordDto) {
		Keyword existingKeyword = keywordRepository.findById(id)
			.orElseThrow(() -> new NullPointerException("Keyword not found"));

		// 기본 정보 업데이트
		existingKeyword.setName(keywordDto.getName());
		existingKeyword.setDescription(keywordDto.getDesc());
		existingKeyword.setFavorite(keywordDto.isFavorite());

		// 계좌 정보 업데이트
		if (keywordDto.getAccount() != null) {
			Account account = accountRepository.findById(keywordDto.getAccount().getId())
				.orElseThrow(() -> new NullPointerException("Account not found"));
			existingKeyword.setAccount(account);
		}

		if (keywordDto.getSubAccount() != null) {
			Account subAccount = accountRepository.findByAccountNumber(keywordDto.getSubAccount().getAccountNumber())
				.orElseThrow(() -> new NullPointerException("Account not found"));
			existingKeyword.setSubAccount(subAccount);
		}

		// 타입별 특정 필드 업데이트
		switch (existingKeyword.getType()) {
			case INQUIRY:
				existingKeyword.setInquiryWord(keywordDto.getInquiryWord());
				break;
			case TRANSFER:
				validateAmountAndCheckEveryTime(keywordDto);
				existingKeyword.setAmount(keywordDto.getAmount());
				existingKeyword.setCheckEveryTime(keywordDto.getCheckEveryTime());
				break;
			case TICKET:
				existingKeyword.setBranch(keywordDto.getBranch());
				break;
			case SETTLEMENT:
				validateAmountAndCheckEveryTime(keywordDto);
				existingKeyword.setGroupMember(keywordDto.getGroupMember());
				existingKeyword.setAmount(keywordDto.getAmount());
				existingKeyword.setCheckEveryTime(keywordDto.getCheckEveryTime());
				break;
		}

		Keyword updatedKeyword = keywordRepository.save(existingKeyword);
		return KeywordMapper.toDto(updatedKeyword);
	}

	@Override
	public ResponseEntity<DeleteResponseDto> removeKeyword(Long id) {
		Optional<Keyword> keyword = keywordRepository.findById(id);
		if (keyword.isPresent()) {
			keywordRepository.delete(keyword.get());
			return ResponseEntity.ok(new DeleteResponseDto(true, "Keyword deleted successfully"));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new DeleteResponseDto(false, "Keyword not found"));
		}
	}

	@Override
	public KeywordResponseDto useKeyword(Long id) throws Exception {
		Keyword keyword = keywordRepository.findById(id)
			.orElseThrow(() -> new KeywordNotFoundException("Keyword not found"));

		return switch (keyword.getType()) {
			case INQUIRY -> useInquiryKeyword(keyword);
			case TRANSFER, TICKET, SETTLEMENT -> useOtherKeywordTypes(keyword);
			default -> throw new InvalidRequestException("Invalid keyword type");
		};
	}

	@Override
	public List<KeywordDto> getKeywordsByUsername(String username) {
		return keywordRepository.findAllByUserUsername(username).stream().map(KeywordMapper::toDto).toList();
	}

	@Override
	public List<KeywordDto> getFavoriteKeywordsByUsername(String username) {
		return keywordRepository.findAllByUserUsernameAndIsFavoriteTrue(username)
			.stream()
			.map(KeywordMapper::toDto)
			.toList();
	}

	private KeywordResponseDto useInquiryKeyword(Keyword keyword) throws AccountNotFoundException {
		LocalDate endDate = LocalDate.now();
		LocalDate startDate = endDate.minusMonths(3); // 최근 3개월 조회

		List<TransactionDto> transactions = inquiryService.getAccountTransactions(
			keyword.getAccount().getId(),
			startDate,
			endDate,
			"all",
			"latest",
			keyword.getInquiryWord()
		);

		KeywordDto keywordDto = KeywordMapper.toDto(keyword);
		return KeywordResponseMapper.toDto(keywordDto, transactions, null);
	}

	private KeywordResponseDto useOtherKeywordTypes(Keyword keyword) {
		KeywordDto keywordDto = KeywordMapper.toDto(keyword);

		// branch string에서 json으로 변환
		BranchDto branchJson = null;
		if (keywordDto.getBranch() != null) {
			try {
				branchJson = objectMapper.readValue(keywordDto.getBranch(), BranchDto.class);
			} catch (JsonProcessingException e) {
				log.error("Error parsing branch JSON", e);
			}
		}

		return KeywordResponseMapper.toDto(keywordDto, null, branchJson);

	}

	private void validateCommonRequest(KeywordDto keywordDto) {
		if (keywordDto.getType() == null) {
			throw new InvalidRequestException("Keyword type is required");
		}
		if (keywordDto.getName() == null || keywordDto.getName().trim().isEmpty()) {
			throw new InvalidRequestException("Keyword name is required");
		}
		if (keywordDto.getDesc() == null || keywordDto.getDesc().trim().isEmpty()) {
			throw new InvalidRequestException("Keyword description is required");
		}
	}

	private void validateInquiryKeyword(KeywordDto keywordDto) {
		if (keywordDto.getAccount() == null) {
			throw new InvalidRequestException("Account is required for INQUIRY keyword");
		}
		if (keywordDto.getInquiryWord() == null || keywordDto.getInquiryWord().trim().isEmpty()) {
			throw new InvalidRequestException("Inquiry word is required for INQUIRY keyword");
		}
	}

	private void validateTransferKeyword(KeywordDto keywordDto) {
		if (keywordDto.getAccount() == null) {
			throw new InvalidRequestException("Account is required for TRANSFER keyword");
		}
		if (keywordDto.getSubAccount() == null) {
			throw new InvalidRequestException("Sub-account is required for TRANSFER keyword");
		}
		validateAmountAndCheckEveryTime(keywordDto);
	}

	private void validateTicketKeyword(KeywordDto keywordDto) {
		if (keywordDto.getBranch() == null || keywordDto.getBranch().trim().isEmpty()) {
			throw new InvalidRequestException("Branch information is required for TICKET keyword");
		}
	}

	private void validateSettlementKeyword(KeywordDto keywordDto) {
		if (keywordDto.getAccount() == null) {
			throw new InvalidRequestException("Account is required for SETTLEMENT keyword");
		}
		if (keywordDto.getGroupMember() == null || keywordDto.getGroupMember().trim().isEmpty()) {
			throw new InvalidRequestException("Group member information is required for SETTLEMENT keyword");
		}
		validateAmountAndCheckEveryTime(keywordDto);
	}

	private void validateMultiKeyword(KeywordDto keywordDto) {
		if (keywordDto.getMultiKeyword() == null) {
			throw new InvalidRequestException("Multi keyword is required for MULTI keyword");
		}
	}

	private void validateAmountAndCheckEveryTime(KeywordDto keywordDto) {
		if (Boolean.TRUE.equals(keywordDto.getCheckEveryTime()) && keywordDto.getAmount() != null) {
			throw new InvalidRequestException("Amount should not be provided when checkEveryTime is true");
		}
		if (Boolean.FALSE.equals(keywordDto.getCheckEveryTime()) && keywordDto.getAmount() == null) {
			throw new InvalidRequestException("Valid amount is required when checkEveryTime is false");
		}
	}

}
