package com.passwords.manager.domain.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.passwords.manager.core.cdi.annotation.Component;
import com.passwords.manager.core.cdi.annotation.Inject;
import com.passwords.manager.domain.model.Credential;
import com.passwords.manager.infrastructure.db.repository.CredentialRepository;
import com.passwords.manager.infrastructure.db.repository.H2Repository;

@Component
public class CredentialServiceImpl implements CredentialService {

	private static final Logger logger = LogManager.getLogger(CredentialServiceImpl.class);

	@Inject(CredentialRepository.class)
	private H2Repository<Credential> credentialRepository;

	@Override
	public Optional<Credential> getBySiteName(String siteName) {
		logger.info("Getting credential for site: {}", siteName);
		return credentialRepository.findBy("siteName", siteName);
	}

	@Override
	public void store(Credential credential) {
		logger.info("Storing credential");
		credentialRepository.store(credential);
	}
}
