package com.passwords.manager.domain.service;

public interface AppKeyService {

	/**
	 * Check if the Application Key used to encrypt/decrypt is registered in the application
	 * 
	 * @return True if it's registered. False if is not registered yet
	 */
	boolean isAppKeyRegistered();

	/**
	 * Store in database one new appKey
	 * 
	 * @param applicationKey The applicationKey to be stored
	 */
	void store(String applicationKey);
}
