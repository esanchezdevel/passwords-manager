package com.passwords.manager.domain.service;

import java.util.List;
import java.util.Optional;

import com.passwords.manager.domain.model.Credential;

public interface CredentialService {

	/**
	 * Store one Credential object in the database
	 * 
	 * @param credential The object to be stored
	 */
	void store(Credential credential);

	/**
	 * Get Credential object for one site looking by it's name
	 * 
	 * @param site The name of the site
	 * @return Optinal of Credential object found
	 */
	Optional<Credential> getBySiteName(String site);

	/**
	 * Get all the credentials stored in the database
	 * 
	 * @return List of Credential objects
	 */
	List<Credential> getAll();
}
