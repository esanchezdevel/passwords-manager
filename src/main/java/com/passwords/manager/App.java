package com.passwords.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

	private static final Logger logger = LogManager.getLogger(App.class);

	// CDI container needed for Jakarta EE (CDI) framework
	private static SeContainer container;

	@Override
	public void init() throws Exception {
		logger.info("Initializing application");

		container = SeContainerInitializer.newInstance().initialize();
		logger.info("Container initialized");
	}

	@Override
	public void start(Stage stage) throws Exception {
	    logger.info("Starting JavaFX application...");

        // Use CDI-injected service
        Label label = new Label("Hello World!!!");

        // Setup UI
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("JavaFX + CDI");
        stage.setScene(scene);
        stage.show();

	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		super.stop();
	}

	public static void main(String[] args) {
		logger.info("Running Passwords Manager");
		launch(args);
	}
}
