package com.passwords.manager.domain.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.passwords.manager.core.cdi.annotation.Component;
import com.passwords.manager.domain.model.Credential;
import com.passwords.manager.infrastructure.db.DatabaseManager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Component
public class CredentialServiceImpl implements CredentialService {

	private static final Logger logger = LogManager.getLogger(CredentialServiceImpl.class);

	@Override
	public Optional<Credential> getBySiteName(String siteName) {
		EntityManager em = DatabaseManager.getEntityManager();
		try {
			TypedQuery<Credential> query = em.createQuery("SELECT c FROM Credential c WHERE c.siteName = '" + siteName + "'", Credential.class);
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
	public void store(Credential credential) {
		EntityManager em = DatabaseManager.getEntityManager();
		em.getTransaction().begin();
		em.persist(credential);
		em.getTransaction().commit();
		em.close();
	}
}
