package com.passwords.manager.application.component;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PopUpWindow {

	public static void show(AlertType type, String title, String header, String msg) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(msg);
		alert.showAndWait();
	}
}
