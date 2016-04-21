package com.lingualeo;

import com.lingualeo.client.ApiClient;
import com.lingualeo.client.TranslateDto;
import com.lingualeo.reader.Word;
import javafx.scene.control.ProgressBar;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class Importer {
    private final List<Word> words;
    private final ApiClient client;
    private ProgressBar progressBar;

    public Importer(List<Word> words, ApiClient client, ProgressBar progressBar) {
        this.words = words;
        this.client = client;
        this.progressBar = progressBar;
        this.progressBar.setVisible(true);
    }

    public Importer(List<Word> words, ApiClient client) {
        this.words = words;
        this.client = client;
    }

    public void startImport() {
        if (words.isEmpty()) {
            System.out.println("You don't have words in the file");
            return;
        }
        System.out.println("Start import to Lingualeo...");

        try {
            client.auth();
        } catch (AuthenticationException | IOException e) {
            System.err.println(e.getMessage());
            return;
        }


        updateProgress(0);
        float count = words.size();
        float i = 0;

        for (Word word : words) {
            try {
                Iterator<TranslateDto> it = client.getTranslates(word.getName()).iterator();
                if(it.hasNext()) {
                    TranslateDto tr = it.next();
                    if (tr.is_user == 1) {
                        System.out.println("Word exists: " + word.getName());
                    } else {
                        client.addWord(word.getName(), tr.value, word.getContext());
                        System.out.println("Word added: " + word.getName());
                    }
                }

                float percent = i / count;
                updateProgress(percent);

                i++;
            } catch (AuthenticationException | IOException e) {
                System.err.println(e.getMessage());
            }
        }
        updateProgress(1);
        System.out.println("End import to Lingualeo...");
    }

    private void updateProgress(double progress) {
        if(progressBar != null) {
            progressBar.setProgress(progress);
        }
    }
}
