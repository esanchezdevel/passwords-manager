package com.passwords.manager.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.passwords.manager.application.service.LoadViewService;
import com.passwords.manager.application.util.Constants;
import com.passwords.manager.core.cdi.App;
import com.passwords.manager.core.cdi.annotation.Inject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
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

	@Inject
	private LoadViewService loadViewService;

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
}
