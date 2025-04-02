package com.passwords.manager.domain.service;

import java.util.Optional;

import com.passwords.manager.domain.model.Credential;

public interface CredentialService {

	void store(Credential credential);

	Optional<Credential> getBySiteName(String site);
}
