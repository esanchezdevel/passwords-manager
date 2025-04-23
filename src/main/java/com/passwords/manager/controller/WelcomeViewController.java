package com.passwords.manager.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.passwords.manager.application.util.Constants;
import com.passwords.manager.application.util.PasswordsUtils;
import com.passwords.manager.core.cdi.App;
import com.passwords.manager.core.cdi.annotation.Inject;
import com.passwords.manager.domain.service.AppKeyService;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WelcomeViewController extends App {

	private static final Logger logger = LogManager.getLogger(WelcomeViewController.class);

	@FXML
	private AnchorPane welcomePane;
	@FXML
	private VBox rootVBox;
	@FXML
	private HBox titleHBox;
	@FXML
	private Label titleLabel;
	@FXML
	private Button addSiteButton;

	@Inject
	private AppKeyService appKeyService;

	@FXML
	public void initialize() {
		logger.debug("Start Initialize");

		rootVBox.setMinWidth(Constants.APP_WIDTH);
		rootVBox.setMaxWidth(Double.MAX_VALUE);

		AnchorPane.setTopAnchor(rootVBox, 0.0);
		AnchorPane.setLeftAnchor(rootVBox, 0.0);
		AnchorPane.setRightAnchor(rootVBox, 0.0);
		AnchorPane.setBottomAnchor(rootVBox, 0.0);

		titleHBox.setMinWidth(Constants.APP_WIDTH);
		titleHBox.setMaxWidth(Double.MAX_VALUE);
		titleHBox.setAlignment(Pos.CENTER);
		titleHBox.setMinHeight(50.0);
		titleHBox.setMaxHeight(50.0);

		VBox.setVgrow(titleHBox, Priority.ALWAYS);

		titleLabel.setText(Constants.APP_TITLE_VERSION);
		titleLabel.setMinHeight(50.0);
		titleLabel.setMaxHeight(50.0);

		VBox.setMargin(titleHBox, new Insets(20, 20, 20, 20)); // top, right, bottom, left
		VBox.setMargin(addSiteButton, new Insets(20, 20, 20, 20)); // top, right, bottom, left

		boolean isAppKeyRegistered = appKeyService.isAppKeyRegistered();

		logger.info("Is App Key registered: {}", isAppKeyRegistered);

		if (!isAppKeyRegistered) {
			logger.info("Show PopUp to register a new App Key");
			showAppKeyPopup();
		}
	}

	private void showAppKeyPopup() {
		Stage popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("App Key");

		Label label = new Label("Introduzca una clave única. Recuerde que esta clave no podrá cambiarse en el futuro.");
		PasswordField inputField = new PasswordField();
		Button okButton = new Button("Aceptar");

		okButton.setOnAction(e -> {
			String enteredKey = inputField.getText();
			logger.info("Entered App Key: " + PasswordsUtils.ofuscate(enteredKey));
			appKeyService.store(enteredKey);
			popupStage.close();
		});

		VBox layout = new VBox(10, label, inputField, okButton);
		layout.setStyle("-fx-padding: 20;");
		popupStage.setScene(new Scene(layout));
		popupStage.showAndWait();
	}

	public void handleAddSite() {
		logger.info("Handle Add Site...");
	}
}
