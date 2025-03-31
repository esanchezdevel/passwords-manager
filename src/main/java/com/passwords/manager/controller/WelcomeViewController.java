package com.passwords.manager.controller;

import com.passwords.manager.core.cdi.App;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WelcomeViewController extends App {

	@FXML
	private Label titleLabel;

	@FXML
	public void initialize() {
		titleLabel.setText("Title Label For Passwords Manager");
	}
}
