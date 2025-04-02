package com.passwords.manager.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.passwords.manager.core.cdi.App;
import com.passwords.manager.core.cdi.annotation.Inject;
import com.passwords.manager.domain.model.Credential;
import com.passwords.manager.domain.service.CredentialService;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WelcomeViewController extends App {

	private static final Logger logger = LogManager.getLogger(WelcomeViewController.class);

	@FXML
	private Label titleLabel;

	@Inject
	private CredentialService credentialService;

	@FXML
	public void initialize() {

		Credential credential = new Credential();
		credential.setSiteName("Test site");
		credential.setSiteUrl("http://test.test");
		credential.setUsername("test-user");
		credential.setPassword("test-password");

		credentialService.store(credential);

		Optional<Credential> storedCredential = credentialService.getBySiteName("Test site");
		if (storedCredential.isPresent())
			logger.info("Stored credential: {}", storedCredential.get());
		else
			logger.info("No credential found in database");

		titleLabel.setText("Title Label For Passwords Manager");
	}
}
