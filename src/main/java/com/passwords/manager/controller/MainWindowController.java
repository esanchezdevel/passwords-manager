package com.passwords.manager.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class MainWindowController {

	@FXML
	private Pane contentPane;

	@FXML
	public void initialize() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/welcome-view.fxml"));
			Parent newView = loader.load();
			contentPane.getChildren().setAll(newView);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
