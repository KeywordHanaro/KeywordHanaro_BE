package com.hana4.keywordhanaro.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.hana4.keywordhanaro.model.entity.keyword.Keyword;
import com.hana4.keywordhanaro.model.entity.keyword.KeywordType;
import com.hana4.keywordhanaro.model.entity.user.User;

import jakarta.persistence.EntityManager;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class KeywordRepositoryTest {
	@Autowired
	private KeywordRepository keywordRepository;

	@Autowired
	private EntityManager em;

	@Test
	public void deleteKeywordTest() {
		Keyword keyword = new Keyword();
		User user = new User();
		user.setId("2160a26a-0a23-4a16-813b-9045b2a5b489");
		keyword.setUser(user);
		keyword.setType(KeywordType.TICKET);
		keyword.setName("keyword");
		keyword.setDescription("keyword description");
		keyword.setSeqOrder(100L);
		keyword.setBranch("branch");
		em.persist(keyword);
		Keyword saveKeyword = keywordRepository.save(keyword);

		keywordRepository.delete(saveKeyword);

		Optional<Keyword> deletedKeyword = keywordRepository.findById(saveKeyword.getId());
		assertThat(deletedKeyword).isEmpty();
	}
}
