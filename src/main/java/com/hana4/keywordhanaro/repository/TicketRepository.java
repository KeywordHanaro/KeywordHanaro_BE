package com.hana4.keywordhanaro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana4.keywordhanaro.model.entity.Ticket;
import com.hana4.keywordhanaro.model.entity.user.User;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
	Optional<Ticket> findByUser(User user);
}
