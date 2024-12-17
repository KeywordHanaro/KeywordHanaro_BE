import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.hana4.keywordhanaro.model.dto.TransactionDto;
import com.hana4.keywordhanaro.model.entity.transaction.TransactionType;
import com.hana4.keywordhanaro.service.InquiryServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InquiryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private InquiryServiceImpl inquiryService;

	@Test
	@Order(1)
	void testGetAccountTransactions() throws Exception {
		Long accountId = 1L;
		LocalDate startDate = LocalDate.of(2023, 1, 1);
		LocalDate endDate = LocalDate.of(2023, 12, 31);

		List<TransactionDto> mockTransactions = createMockTransactions();
		when(inquiryService.getAccountTransactions(eq(accountId), eq(startDate), eq(endDate),
			eq("all"), eq("latest"), eq("")))
			.thenReturn(mockTransactions);

		mockMvc.perform(get("/inquiry/{accountId}", accountId)
				.param("startDate", startDate.toString())
				.param("endDate", endDate.toString())
				.param("transactionType", "all")
				.param("sortOrder", "latest")
				.param("searchWord", ""))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$.length()").value(mockTransactions.size()))
			.andExpect(jsonPath("$[0].id").value(mockTransactions.get(0).getId()))
			.andExpect(jsonPath("$[0].amount").value(mockTransactions.get(0).getAmount()));
	}

	@Test
	@Order(2)
	void testGetAccountTransactionsWithFilters() throws Exception {
		Long accountId = 1L;
		LocalDate startDate = LocalDate.of(2023, 1, 1);
		LocalDate endDate = LocalDate.of(2023, 12, 31);

		List<TransactionDto> mockTransactions = createMockTransactions().subList(0, 1);
		when(inquiryService.getAccountTransactions(eq(accountId), eq(startDate), eq(endDate),
			eq("TRANSFER"), eq("oldest"), eq("test")))
			.thenReturn(mockTransactions);

		mockMvc.perform(get("/inquiry/{accountId}", accountId)
				.param("startDate", startDate.toString())
				.param("endDate", endDate.toString())
				.param("transactionType", "TRANSFER")
				.param("sortOrder", "oldest")
				.param("searchWord", "test"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$.length()").value(mockTransactions.size()))
			.andExpect(jsonPath("$[0].type").value("TRANSFER"));
	}

	private List<TransactionDto> createMockTransactions() {
		List<TransactionDto> transactions = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			TransactionDto dto = TransactionDto.builder()
				.id((long)i)
				.amount(BigDecimal.valueOf(100.0 * (i + 1)))
				.type(TransactionType.DEPOSIT)
				.build();
			transactions.add(dto);
		}
		return transactions;
	}
}
