package com.passwords.manager.application.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.passwords.manager.core.cdi.annotation.Component;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

@Component
public class LoadViewServiceImpl implements LoadViewService {

    private static final Logger logger = LogManager.getLogger(LoadViewServiceImpl.class);

    @Override
    public void load(StackPane contentPane, String view) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
			Parent newView = loader.load();
			StackPane.setMargin(newView, new Insets(0));
			contentPane.getChildren().setAll(newView);
		} catch (Exception e) {
            logger.error("Error loading view " + view, e);
		}
    }
}
