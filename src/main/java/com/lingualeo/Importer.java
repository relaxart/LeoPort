package com.lingualeo;

import com.lingualeo.client.ApiClient;
import com.lingualeo.reader.Word;
import javafx.scene.control.ProgressBar;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.List;

public class Importer {
    private final List<Word> words;
    private final String login;
    private final String password;
    private final ProgressBar progressBar;

    public Importer(List<Word> words, String login, String password, ProgressBar progressBar) {
        this.words = words;
        this.login = login;
        this.password = password;
        this.progressBar = progressBar;
    }

    public void startImport() {
        if (words.isEmpty()) {
            System.out.println("You don't have words in the file");
            return;
        }
        System.out.println("Start import to Lingualeo...");
        ApiClient client = new ApiClient(login, password);

        try {
            client.auth();
        } catch (AuthenticationException | IOException e) {
            System.err.println(e.getMessage());
            return;
        }

        progressBar.setVisible(true);
        progressBar.setProgress(0);
        float count = words.size();
        float i = 0;

        for (Word word : words) {
            try {
                List<ApiClient.Translations.Translate> translates = client.getTranslates(word.getName());
                ApiClient.Translations.Translate tr = translates.iterator().next();
                if (tr.is_user == 1) {
                    System.out.println("Word exists: " + word.getName());
                } else {
                    client.addWord(word.getName(), tr.value, word.getContext());
                    System.out.println("Word added: " + word.getName());
                }

                float percent = i / count;
                progressBar.setProgress(percent);

                i++;
            } catch (AuthenticationException | IOException e) {
                System.err.println(e.getMessage());
            }
        }
        progressBar.setProgress(1);
        System.out.println("End import to Lingualeo...");
    }
}
