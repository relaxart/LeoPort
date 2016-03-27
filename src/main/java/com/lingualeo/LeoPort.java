package com.lingualeo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class LeoPort extends Application {

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

            OutputStream output = new TextAreaOutputStream(logArea, System.out);
            PrintStream printOut = new PrintStream(output);
            System.setOut(printOut);
            System.setErr(printOut);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}