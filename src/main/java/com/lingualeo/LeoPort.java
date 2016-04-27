package com.lingualeo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LeoPort extends Application {
    private static final Logger logger = Logger.getLogger(LeoPort.class.getName());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("leoport.fxml"));
            Scene scene = new Scene(root, 420, 375);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            TextArea logArea = (TextArea) scene.lookup("#textField");

            stage.setMaxHeight(420);
            stage.setMaxWidth(600);
            stage.setTitle("LeoPort");
            stage.setScene(scene);
            stage.show();

            LogManager.getLogManager().reset();
            Logger importLogger = Logger.getLogger(Importer.class.getName());
            importLogger.setLevel(Level.ALL);
            importLogger.addHandler(new LogHandler(logArea));
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}