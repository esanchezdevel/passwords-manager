package com.passwords.manager.infrastructure.db.repository;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.passwords.manager.core.cdi.annotation.Component;
import com.passwords.manager.domain.model.Credential;
import com.passwords.manager.infrastructure.db.DatabaseManager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Component
public class CredentialRepository implements H2Repository<Credential> {

	private static final Logger logger = LogManager.getLogger(CredentialRepository.class);

	@Override
	public void store(Credential credential) {
		EntityManager em = DatabaseManager.getEntityManager();
		em.getTransaction().begin();
		em.persist(credential);
		em.getTransaction().commit();
		em.close();
	}

	@Override
	public Optional<Credential> findById(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Optional<Credential> findBy(String columnName, String value) {
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

	@Override
	public List<Credential> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
