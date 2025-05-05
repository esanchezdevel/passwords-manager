package com.passwords.manager.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.passwords.manager.application.component.PopUpWindow;
import com.passwords.manager.application.service.LoadViewService;
import com.passwords.manager.application.util.CommonUtils;
import com.passwords.manager.application.util.Constants;
import com.passwords.manager.core.cdi.App;
import com.passwords.manager.core.cdi.annotation.Inject;
import com.passwords.manager.domain.model.Credential;
import com.passwords.manager.domain.service.CredentialService;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class NewSiteViewController extends App {

	private static final Logger logger = LogManager.getLogger(NewSiteViewController.class);

	@FXML
	private AnchorPane welcomePane;
	@FXML
	private VBox rootVBox;
	@FXML
	private HBox titleHBox;
	@FXML
	private Label titleLabel;
	@FXML
	private TextField siteNameTextField, siteUrlTextField, siteUsernameTextField;
	@FXML
	private PasswordField sitePasswordTextField;

	@Inject
	private LoadViewService loadViewService;

	@Inject
	private CredentialService credentialService;

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
	}

	@FXML
	public void goToWelcome() {
		logger.info("Go to welcome view...");

		loadViewService.load(MainWindowController.contentPaneCopy, "/welcome-view.fxml");
	}

	@FXML
	public void saveSite() {
		logger.info("Saving new site");

		if (CommonUtils.isEmpty(siteNameTextField.getText()) || CommonUtils.isEmpty(siteUrlTextField.getText()) || 
			CommonUtils.isEmpty(siteUsernameTextField.getText()) || CommonUtils.isEmpty(sitePasswordTextField.getText())) {
			String errorMsg = "Mandatory values not present. Please, review the form";
			logger.error(errorMsg);

			PopUpWindow.show(AlertType.ERROR, "Validation Error", "Missing or Invalid Input", errorMsg);
			return;
		}

		Optional<Credential> storedCredential = credentialService.getBySiteName(siteNameTextField.getText());

		if (storedCredential.isPresent()) {
			PopUpWindow.show(AlertType.ERROR, "Validation Error", "Something goes wrong!", "A site with name '" + siteNameTextField.getText() + "' is already registered");
			return;
		}

		Credential credential = new Credential();
		credential.setSiteName(siteNameTextField.getText());
		credential.setSiteUrl(siteUrlTextField.getText());
		credential.setUsername(siteUsernameTextField.getText());
		credential.setPassword(sitePasswordTextField.getText());
		credentialService.store(credential);

		loadViewService.load(MainWindowController.contentPaneCopy, "/welcome-view.fxml");
	}
}
