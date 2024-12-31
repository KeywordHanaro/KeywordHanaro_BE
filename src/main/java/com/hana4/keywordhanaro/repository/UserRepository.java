package com.hana4.keywordhanaro.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hana4.keywordhanaro.model.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findFirstByUsername(String username);

	@Query("SELECT distinct u.kakaoUUID FROM User u WHERE u.tel = :tel")
	String findKakaoUUIDByTel(@Param("tel") String tel);
}
