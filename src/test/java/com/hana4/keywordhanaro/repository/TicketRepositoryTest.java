package com.hana4.keywordhanaro.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana4.keywordhanaro.model.entity.Ticket;
import com.hana4.keywordhanaro.model.entity.keyword.Keyword;
import com.hana4.keywordhanaro.model.entity.keyword.KeywordType;
import com.hana4.keywordhanaro.model.entity.user.User;
import com.hana4.keywordhanaro.model.entity.user.UserStatus;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TicketRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private KeywordRepository keywordRepository;

	@BeforeAll
	void beforeAll() {
		if (userRepository.findFirstByUsername("insunID").isEmpty()) {
			User testUser = new User("insunID", "insss123", "김인선", UserStatus.ACTIVE, 0);
			userRepository.save(testUser);
		}

		if (keywordRepository.findByName("inssTicketKeyword").isEmpty()) {
			User testUser = userRepository.findFirstByUsername("insunID").orElseThrow();
			String testBranch = """
				{
					"address_name": "서울 성동구 성수동2가 289-10",
					"distance": "117",
					"id": "1841540654",
					"phone": "02-462-7627",
					"place_name": "하나은행 성수역지점",
					"road_address_name": "서울 성동구 성수이로 113",
					"x": "127.05717861008637",
					"y": "37.54512527783082"
				}
				""";

			Keyword testKeyword = new Keyword(testUser, KeywordType.TICKET, 1L, "inssTicketKeyword", true, testBranch,
				"번호표 키워드 테스트 통과 ?!");
			keywordRepository.save(testKeyword);
		}
	}

	@AfterEach
	void tearDown() {
		ticketRepository.deleteAll();
	}

	@Test
	void createTicketTest() throws JsonProcessingException {
		Keyword testKeyword = keywordRepository.findByName("inssTicketKeyword").orElseThrow();

		String testBranch = testKeyword.getBranch();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode branchNode = objectMapper.readTree(testBranch);

		Ticket ticket = new Ticket(branchNode.get("id").asLong(), branchNode.get("place_name").asText(),
			testKeyword.getUser(), 12L, 111L, (byte)1);

		Ticket savedTicket = ticketRepository.save(ticket);

		assertThat(savedTicket).isNotNull();
		assertThat(savedTicket.getId()).isNotNull();
		assertThat(savedTicket.getUser().getUsername()).isEqualTo("insunID");
		assertThat(savedTicket.getBranchId()).isEqualTo(branchNode.get("id").asLong());
		assertThat(savedTicket.getBranchName()).isEqualTo(branchNode.get("place_name").asText());
		assertThat(savedTicket.getWaitingNumber()).isEqualTo(111L);
		assertThat(savedTicket.getWaitingGuest()).isEqualTo(12L);
		assertThat(savedTicket.getWorkNumber()).isEqualTo((byte)1);
	}
}
