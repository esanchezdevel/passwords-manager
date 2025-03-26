package com.passwords.manager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WelcomeViewController {

	@FXML
	private Label titleLabel;

	@FXML
	public void initialize() {
		titleLabel.setText("Title Label For Passwords Manager");
	}
}
