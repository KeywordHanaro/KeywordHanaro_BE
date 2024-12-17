package com.hana4.keywordhanaro.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hana4.keywordhanaro.model.entity.transaction.Transaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class InquiryCustomRepositoryImpl implements InquiryCustomRepository {
	@PersistenceContext
	private EntityManager entityManager;

	public List<Transaction> findTransactions(
		Long accountId,
		LocalDateTime startDateTime,
		LocalDateTime endDateTime,
		String transactionType,
		String searchWord,
		String sortOrder
	) {
		CriteriaWrapper cw = initCriteria();
		List<Predicate> predicates = buildCriteria(
			cw,
			accountId,
			startDateTime,
			endDateTime,
			transactionType,
			searchWord
		);

		cw.cq.where(predicates.toArray(new Predicate[0]));

		if ("desc".equalsIgnoreCase(sortOrder)) {
			cw.cq.orderBy(cw.cb.desc(cw.root.get("createAt")));
		} else {
			cw.cq.orderBy(cw.cb.asc(cw.root.get("createAt")));
		}

		TypedQuery<Transaction> query = entityManager.createQuery(cw.cq);
		return query.getResultList();
	}

	private CriteriaWrapper initCriteria() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Transaction> cq = cb.createQuery(Transaction.class);
		Root<Transaction> root = cq.from(Transaction.class);
		return new CriteriaWrapper(cb, cq, root);
	}

	private List<Predicate> buildCriteria(
		CriteriaWrapper cw,
		Long accountId,
		LocalDateTime startDateTime,
		LocalDateTime endDateTime,
		String transactionType,
		String searchWord
	) {
		CriteriaBuilder cb = cw.cb;
		Root<Transaction> root = cw.root;
		List<Predicate> predicates = new ArrayList<>();

		// (t.fromAccount.id = :accountId OR t.toAccount.id = :accountId)
		// Predicate fromMatch = cb.equal(root.get("fromAccount").get("id"), accountId);
		// Predicate toMatch = cb.equal(root.get("toAccount").get("id"), accountId);
		predicates.add(cb.equal(root.get("account").get("id"), accountId));

		// t.createAt BETWEEN :startDateTime AND :endDateTime
		if (startDateTime != null && endDateTime != null) {
			predicates.add(cb.between(root.get("createAt"), startDateTime, endDateTime));
		}

		// (transactionType = 'all' OR t.type = transactionType)
		if (transactionType != null && !"all".equalsIgnoreCase(transactionType)) {
			predicates.add(cb.equal(root.get("type"), transactionType));
		}

		// (:searchWord IS NULL OR LOWER(t.alias) LIKE LOWER('%searchWord%'))
		if (searchWord != null && !searchWord.trim().isEmpty()) {
			String pattern = "%" + searchWord.toLowerCase() + "%";
			predicates.add(cb.like(cb.lower(root.get("alias")), pattern));
		}

		return predicates;
	}

	private static class CriteriaWrapper {
		private final CriteriaBuilder cb;
		private final CriteriaQuery<Transaction> cq;
		private final Root<Transaction> root;

		CriteriaWrapper(CriteriaBuilder cb, CriteriaQuery<Transaction> cq, Root<Transaction> root) {
			this.cb = cb;
			this.cq = cq;
			this.root = root;
		}
	}
}
