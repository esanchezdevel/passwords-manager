package com.passwords.manager.infrastructure.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DatabaseManager {

	private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("passwordsManagerUnit");

	public static EntityManager getEntityManager() {
		return FACTORY.createEntityManager();
	}
}
