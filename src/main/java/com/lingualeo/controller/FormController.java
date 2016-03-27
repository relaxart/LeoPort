package com.lingualeo.controller;

import com.lingualeo.Importer;
import com.lingualeo.reader.BaseReader;
import com.lingualeo.reader.ReaderFactory;
import com.lingualeo.reader.Word;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class FormController {
    public PasswordField passwordField;
    public TextField emailField;
    public Button startButton;
    public TextArea textField;
    public ProgressBar progressBar;
    private List<Word> words;

    @FXML
    protected void handleSubmitButtonAction() {
        Thread t = new Thread(
                () -> {
                    String password = passwordField.getText();
                    Importer importer = new Importer(words, emailField.getText(), password, progressBar);
                    startButton.setDisable(true);
                    importer.startImport();
                }
        );
        t.start();
    }

    @FXML
    protected void handleFileChooserAction() {
        this.startButton.setDisable(true);
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Files (*.txt, *.db)", "*.txt", "*.db");
        chooser.getExtensionFilters().add(filter);
        File file = chooser.showOpenDialog(this.startButton.getScene().getWindow());

        if (file != null) {
            BaseReader reader = ReaderFactory.create(file);
            words = reader.read();
            this.startButton.setDisable(false);
        }
    }
}
