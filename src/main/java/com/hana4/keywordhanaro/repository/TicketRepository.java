package com.hana4.keywordhanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana4.keywordhanaro.model.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
