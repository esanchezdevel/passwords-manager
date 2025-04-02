package com.passwords.manager.infrastructure.db.repository;

import java.util.List;
import java.util.Optional;

public interface H2Repository<T> {

	void store(T object);

	Optional<T> findById(Long id);

	Optional<T> findBy(String columnName, String value);

	List<T> findAll();
}
