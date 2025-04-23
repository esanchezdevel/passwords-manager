package com.passwords.manager.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class MainWindowController {

	@FXML
	private StackPane contentPane;

	public static StackPane contentPaneCopy;

	@FXML
	public void initialize() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/welcome-view.fxml"));
			Parent newView = loader.load();
			StackPane.setMargin(newView, new Insets(0));
			contentPane.getChildren().setAll(newView);
			contentPaneCopy = contentPane;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
