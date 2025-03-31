package com.passwords.manager.controller;

import com.passwords.manager.core.cdi.App;
import com.passwords.manager.core.cdi.DependencyInjection;
import com.passwords.manager.core.cdi.annotation.Inject;
import com.passwords.manager.domain.service.TestService;
import com.passwords.manager.domain.service.TestServiceBis;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WelcomeViewController extends App {

	@Inject(TestServiceBis.class)
	private TestService testService;

	@FXML
	private Label titleLabel;

	@FXML
	public void initialize() {
		titleLabel.setText("Title Label For Passwords Manager");
		System.out.println("TEST--result: " + testService.testMethod("Test"));
	}
}
