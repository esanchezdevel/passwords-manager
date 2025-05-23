package com.passwords.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.passwords.manager.application.util.Constants;
import com.passwords.manager.core.cdi.DependencyInjection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

	private static final Logger logger = LogManager.getLogger(Main.class);

	@Override
	public void init() throws Exception {
		logger.info("Initializing application");
	}

	@Override
	public void start(Stage stage) throws Exception {
	    logger.info("Starting JavaFX application...");

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/main-view.fxml"));
		
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        Scene scene = new Scene(loader.load());

		// Application size and position
		stage.setMaxWidth(Double.MAX_VALUE);
		stage.setMinWidth(Constants.APP_WIDTH);
		stage.setMaxHeight(screenBounds.getHeight());
		stage.setMinHeight(screenBounds.getHeight());
		stage.setX((screenBounds.getWidth() / 2.0) - (Constants.APP_WIDTH / 2.0));
		stage.setY(screenBounds.getMinY());

        stage.setTitle(Constants.APP_TITLE_VERSION);
		stage.setResizable(true);
        stage.setScene(scene);
        stage.show();

	}

	@Override
	public void stop() throws Exception {
		super.stop();
	}

	public static void main(String[] args) {
		logger.info("Running Passwords Manager");
		DependencyInjection.load();
		DependencyInjection.injectDependencies(new Main());
		launch(args);
	}
}
