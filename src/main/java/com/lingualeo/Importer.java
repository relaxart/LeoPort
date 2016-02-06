package com.lingualeo;

import com.lingualeo.client.ApiClient;
import com.lingualeo.client.Translate;
import com.lingualeo.handler.Word;

import javax.naming.AuthenticationException;
import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class Importer {
    private final List<Word> words;
    private final String login;
    private final String password;
    private final JProgressBar progressBar;

    public Importer(List<Word> words, String login, String password, JProgressBar progressBar) {
        this.words = words;
        this.login = login;
        this.password = password;
        this.progressBar = progressBar;
    }

    public void startImport() {
        if (words.isEmpty()) {
            System.out.println("You don't have words in file");
            return;
        }
        System.out.println("Start import to Application...");
        ApiClient client = new ApiClient(login, password);

        try {
            client.auth();
        } catch (AuthenticationException | IOException e) {
            System.err.println(e.getMessage());
            return;
        }

        progressBar.setValue(0);
        float count = words.size();
        float i = 0;

        for (Word word : words) {
            try {
                List<Translate> translates = client.getTranslates(word.getName());
                Translate tr = translates.iterator().next();
                if (tr.isUser) {
                    System.out.println("Word exist: " + word.getName());
                } else {
                    client.addWord(word.getName(), tr.value, word.getContext());
                    System.out.println("Added word: " + word.getName());
                }
                int percent = (int) (i * 100 / count);
                progressBar.setValue(percent);
                i++;
            } catch (AuthenticationException | IOException e) {
                System.err.println(e.getMessage());
            }
        }
        System.out.println("End import to Application...");
    }
}
