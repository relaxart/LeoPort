package com.lingualeo;

import com.lingualeo.handler.BaseHandler;
import com.lingualeo.handler.KindleHandler;
import com.lingualeo.handler.TextHandler;
import com.lingualeo.handler.Word;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Application {
    private static List<Word> words = new ArrayList<>();

    public static void main(String args[]) {
        JFrame frame = new JFrame("Lingualeo word import");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel(new GridBagLayout());
        frame.getContentPane().add(panel, BorderLayout.NORTH);

        JTextField loginField = createLoginButton(panel);
        JPasswordField passwordField = createPasswordField(panel);
        JButton fileButton = createFileButton(panel);
        JButton startButton = createStartButton(panel);
        JTextArea logArea = createLogArea(panel);
        JProgressBar progressBar = createProgressBar(panel);

        fileButton.addActionListener(new ChooseFileAction(startButton));
        startButton.addActionListener(new StartProcessingAction(loginField, passwordField, progressBar));

        frame.getContentPane().validate();
        frame.getContentPane().repaint();

        OutputStream output = new TextAreaOutputStream(logArea, System.out);
        PrintStream printOut = new PrintStream(output);
        System.setOut(printOut);
        System.setErr(printOut);
    }

    private static JTextArea createLogArea(JPanel panel) {
        JTextArea area = new JTextArea(5, 20);
        DefaultCaret caret = (DefaultCaret) area.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        area.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(area);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 3;
        constraints.gridheight = 3;
        panel.add(scrollPane, constraints);
        return area;
    }

    private static JProgressBar createProgressBar(JPanel panel) {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setValue(0);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 3;
        panel.add(progressBar, constraints);
        return progressBar;
    }

    private static JTextField createLoginButton(JPanel panel) {
        GridBagConstraints constraints = new GridBagConstraints();
        JTextField loginField = new JTextField();
        loginField.setColumns(15);
        JLabel loginLabel = new JLabel("Email");
        loginLabel.setLabelFor(loginField);
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(loginLabel, constraints);

        constraints.gridx = 1;
        panel.add(loginField, constraints);

        return loginField;
    }

    private static JPasswordField createPasswordField(JPanel panel) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 1;
        JPasswordField passwordField = new JPasswordField();
        passwordField.setColumns(15);
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setLabelFor(passwordField);
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(passwordLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(passwordField, constraints);

        return passwordField;
    }

    private static JButton createFileButton(JPanel panel) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        JButton fileButton = new JButton("Choose file for export ...");
        panel.add(fileButton, constraints);

        return fileButton;
    }

    private static JButton createStartButton(JPanel panel) {
        GridBagConstraints constraints = new GridBagConstraints();
        JButton startButton = new JButton("Start export");
        startButton.setEnabled(false);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        panel.add(startButton, constraints);

        return startButton;
    }

    static class ChooseFileAction implements ActionListener {
        private final JButton startButton;

        public ChooseFileAction(JButton start) {
            this.startButton = start;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            this.startButton.setEnabled(false);
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Available files", "txt", "text", "db");
            chooser.setFileFilter(filter);
            int isChoose = chooser.showOpenDialog((JButton) actionEvent.getSource());
            if (isChoose == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                String fileName = file.getName();

                int i = fileName.lastIndexOf('.');
                BaseHandler handler;
                switch (fileName.substring(i + 1)) {
                    case "db":
                        handler = new KindleHandler(file);
                        break;
                    default:
                        handler = new TextHandler(file);
                        break;
                }

                handler.read();
                words = handler.getWords();
                this.startButton.setEnabled(true);
            }
        }
    }

    static class StartProcessingAction implements ActionListener {
        private final JProgressBar progressBar;
        private final JTextField loginField;
        private final JPasswordField passwordField;

        public StartProcessingAction(JTextField login, JPasswordField password, JProgressBar progressBar) {
            this.loginField = login;
            this.passwordField = password;
            this.progressBar = progressBar;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Thread t = new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            String password = new String(passwordField.getPassword());
                            Importer importer = new Importer(words, loginField.getText(), password, progressBar);
                            importer.startImport();
                        }
                    }
            );
            t.start();
        }
    }
}