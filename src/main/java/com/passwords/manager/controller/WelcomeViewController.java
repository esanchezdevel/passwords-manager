package com.passwords.manager.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.passwords.manager.application.util.Constants;
import com.passwords.manager.core.cdi.App;
import com.passwords.manager.core.cdi.annotation.Inject;
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
		logger.debug("Start Initialize");

		titleLabel.setText(Constants.APP_TITLE_VERSION);
	}
}
