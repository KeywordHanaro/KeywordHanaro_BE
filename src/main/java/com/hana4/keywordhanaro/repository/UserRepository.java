package com.hana4.keywordhanaro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hana4.keywordhanaro.model.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findFirstByUsername(String username);
}
