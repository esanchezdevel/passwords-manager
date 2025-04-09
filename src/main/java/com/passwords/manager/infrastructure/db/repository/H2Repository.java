package com.passwords.manager.infrastructure.db.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.passwords.manager.infrastructure.db.DatabaseManager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public interface H2Repository<T> {

	static final Logger logger = LogManager.getLogger(CredentialRepository.class);

	// method to get the Generic type from the Proxy created during Dependency injection
	Class<T> getEntityClass();

	/**
	 * Store a new object in database
	 * 
	 * @param object The object to be stored
	 */
	default void store(T object) {
		logger.debug("Storing object");
		EntityManager em = DatabaseManager.getEntityManager();
		em.getTransaction().begin();
		em.persist(object);
		em.getTransaction().commit();
		em.close();
		logger.debug("Object stored");
	}

	/**
	 * Find an object in database by Id
	 * 
	 * @param id identifier of the object
	 * @return Optional of Object found. Empty Optional if no Object were found
	 */
	default Optional<T> findById(Long id) {
		logger.debug("Looking for object by id: {}", id);

		// method to get the Generic type from the Proxy created during Dependency injection
		Class<T> clazz = getEntityClass();

		logger.debug("Class to use: {}", clazz);

		EntityManager em = DatabaseManager.getEntityManager();
		try {
			TypedQuery<T> query = em.createQuery("SELECT c FROM " + clazz.getSimpleName() +" c WHERE c.id = '" + id + "'", clazz);
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

	/**
	 * Find objects in database by a list of parameters
	 * 
	 * @param params Map that contains the parameters used to filter. The 
	 *               key is the column name, and the value is the value to filter by
	 * @return A List of Objects found.
	 * @throws NoResultException If no result can be retrieved from database.
	 */
	default List<T> findBy(Map<String, String> params) throws NoResultException {
		logger.debug("Looking for object by parameters: {}", params);

		// method to get the Generic type from the Proxy created during Dependency injection
		Class<T> clazz = getEntityClass();

		logger.debug("Class to use: {}", clazz);

		EntityManager em = DatabaseManager.getEntityManager();
		try {
			StringBuilder queryString = new StringBuilder("SELECT c FROM ").append(clazz.getSimpleName()).append(" c WHERE ");
			params.forEach((columnName, value) -> queryString.append("c.").append(columnName).append(" = '").append(value).append("'"));

			TypedQuery<T> query = em.createQuery(queryString.toString(), clazz);
			List<T> result = query.getResultList();

			if (result != null)
				return result;
		} catch (Exception e) {
			logger.error("Error executing query.", e);
			throw new NoResultException("Error executing query: " + e.getMessage());
		} finally {
			em.close();
		}
		throw new NoResultException("No objects " + clazz.getSimpleName() + " found filtering by parameters " + params);
	}

	/**
	 * Find all objects in database
	 * 
	 * @return A List of Objects found.
	 * @throws NoResultException If no result can be retrieved from database.
	 */
	default List<T> findAll() {
		logger.debug("Looking for all objects");

		// method to get the Generic type from the Proxy created during Dependency injection
		Class<T> clazz = getEntityClass();

		logger.debug("Class to use: {}", clazz);

		EntityManager em = DatabaseManager.getEntityManager();
		try {
			StringBuilder queryString = new StringBuilder("SELECT c FROM ").append(clazz.getSimpleName()).append(" c");

			TypedQuery<T> query = em.createQuery(queryString.toString(), clazz);
			List<T> result = query.getResultList();

			if (result != null)
				return result;
		} catch (Exception e) {
			logger.error("Error executing query.", e);
			throw new NoResultException("Error executing query: " + e.getMessage());
		} finally {
			em.close();
		}
		throw new NoResultException("No objects " + clazz.getSimpleName() + " found in database");
	}
}
