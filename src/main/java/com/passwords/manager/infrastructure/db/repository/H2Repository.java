package com.passwords.manager.infrastructure.db.repository;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.passwords.manager.domain.model.Credential;
import com.passwords.manager.infrastructure.db.DatabaseManager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public interface H2Repository<T> {

	static final Logger logger = LogManager.getLogger(CredentialRepository.class);

	default void store(Credential credential) {
		EntityManager em = DatabaseManager.getEntityManager();
		em.getTransaction().begin();
		em.persist(credential);
		em.getTransaction().commit();
		em.close();
	}

	default Optional<Credential> findById(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	default Optional<Credential> findBy(String columnName, String value) {
				EntityManager em = DatabaseManager.getEntityManager();
		try {
			TypedQuery<Credential> query = em.createQuery("SELECT c FROM Credential c WHERE c." + columnName + " = '" + value + "'", Credential.class);
			Credential credential = query.getSingleResult();

			if (credential != null)
				return Optional.of(credential);
		} catch (Exception e) {
			logger.error("Error executing query.", e);
		} finally {
			em.close();
		}
		return Optional.empty();
	}

	default List<Credential> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
