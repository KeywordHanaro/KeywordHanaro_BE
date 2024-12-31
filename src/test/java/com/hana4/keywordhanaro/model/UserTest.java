package com.hana4.keywordhanaro.model;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.hana4.keywordhanaro.model.entity.Ticket;
import com.hana4.keywordhanaro.model.entity.user.User;
import com.hana4.keywordhanaro.model.entity.user.UserStatus;

public class UserTest {
	@Test
	void userTest() {
		User user = new User("", "", "", "", UserStatus.ACTIVE, "", "", 1, new Ticket(), LocalDateTime.now(),
			LocalDateTime.now());
		User auser = new User("", "", "", "", UserStatus.ACTIVE, "", "", 1, new Ticket(), LocalDateTime.now(),
			LocalDateTime.now());
		Assertions.assertThat(user).isNotEqualTo(auser);

		User user1 = new User("", "", "", "", UserStatus.ACTIVE, "", "", 1, "");
		User auser1 = new User("", "", "", "", UserStatus.ACTIVE, "", "", 1, "");
		User user2 = new User("", "", "", UserStatus.ACTIVE, "", "", 1, "");
		User auser2 = new User("", "", "", UserStatus.ACTIVE, "", "", 1, "");
		User user3 = new User("", "", "", UserStatus.ACTIVE, "", "", 1);
		User auser3 = new User("", "", "", UserStatus.ACTIVE, "", "", 1);
		User user4 = new User("", "", "", UserStatus.ACTIVE, 1);
		User auser4 = new User("", "", "", UserStatus.ACTIVE, 1);
		User user5 = new User("", "", UserStatus.ACTIVE, 1);
		User auser5 = new User("", "", UserStatus.ACTIVE, 1);

		Assertions.assertThat(user1).isEqualTo(auser1);
		Assertions.assertThat(user2).isEqualTo(auser2);
		Assertions.assertThat(user3).isEqualTo(auser3);
		Assertions.assertThat(user4).isEqualTo(auser4);
		Assertions.assertThat(user5).isEqualTo(auser5);
	}
}
