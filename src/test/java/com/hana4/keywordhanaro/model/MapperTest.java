package com.hana4.keywordhanaro.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.hana4.keywordhanaro.model.dto.AccountDto;
import com.hana4.keywordhanaro.model.dto.AccountUserDto;
import com.hana4.keywordhanaro.model.dto.DocumentDto;
import com.hana4.keywordhanaro.model.entity.document.Document;
import com.hana4.keywordhanaro.model.entity.document.DocumentType;
import com.hana4.keywordhanaro.model.entity.user.User;
import com.hana4.keywordhanaro.model.mapper.DocumentMapper;
import com.hana4.keywordhanaro.model.mapper.UserAccountMapper;

public class MapperTest {
	@Test
	public void testToDtoWithNullUser() {
		AccountUserDto result = UserAccountMapper.toDto(null);
		assertNull(result);
	}

	@Test
	public void testToDtoWithValidUser() {
		User user = new User();
		user.setId("xxx");
		user.setName("Test User");

		AccountUserDto result = UserAccountMapper.toDto(user);

		assertNotNull(result);
		assertEquals("xxx", result.getId());
		assertEquals("Test User", result.getName());
	}

	@Test
	public void testToEntityWithNull() {
		Document result = DocumentMapper.toEntity(null);
		assertNull(result);
	}


}
