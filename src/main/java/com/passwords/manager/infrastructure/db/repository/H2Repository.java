package com.passwords.manager.infrastructure.db.repository;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.passwords.manager.infrastructure.db.DatabaseManager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public interface H2Repository<T> {

	static final Logger logger = LogManager.getLogger(CredentialRepository.class);

	// method to get the Generic type from the Proxy created during Dependency injection
	Class<T> getEntityClass();

	default void store(T object) {
		logger.debug("Storing object");
		EntityManager em = DatabaseManager.getEntityManager();
		em.getTransaction().begin();
		em.persist(object);
		em.getTransaction().commit();
		em.close();
		logger.debug("Object stored");
	}

	default Optional<T> findById(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	default Optional<T> findBy(String columnName, String value) {
		logger.debug("Looking for object by columnName: {} and value: {}", columnName, value);

		// method to get the Generic type from the Proxy created during Dependency injection
		Class<T> clazz = getEntityClass();

		logger.debug("Class to use: {}", clazz);

		EntityManager em = DatabaseManager.getEntityManager();
		try {
			TypedQuery<T> query = em.createQuery("SELECT c FROM " + clazz.getSimpleName() +" c WHERE c." + columnName + " = '" + value + "'", clazz);
			T result = query.getSingleResult();

			if (result != null)
				return Optional.of(result);
		} catch (Exception e) {
			logger.error("Error executing query.", e);
		} finally {
			em.close();
		}
		return Optional.empty();
	}

	default List<T> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
