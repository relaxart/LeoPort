package com.lingualeo;

import com.lingualeo.client.ApiClient;
import com.lingualeo.client.TranslateDto;
import com.lingualeo.reader.Word;
import javafx.scene.control.ProgressBar;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Importer {
    private final List<Word> words;
    private final ApiClient client;
    private static final Logger logger = Logger.getLogger(Importer.class.getName());
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
            logger.warning("You don't have words in the file");
            return;
        }
        logger.finest("Start import to Lingualeo...");
        logger.finest("Authentication to Lingualeo...");
        try {
            client.auth();
        } catch (AuthenticationException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return;
        }
        
        updateProgress(0);
        float count = words.size();
        float i = 0;

        for (Word word : words) {
            try {
                Iterator<TranslateDto> it = client.getTranslates(word.getName()).iterator();
                processWord(word, it);

                float percent = i / count;
                updateProgress(percent);

                i++;
            } catch (AuthenticationException | IOException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        updateProgress(1);
        logger.finest("End import to Lingualeo...");
    }

    private void processWord(Word word, Iterator<TranslateDto> it) throws AuthenticationException, IOException {
        if(it.hasNext()) {
            TranslateDto tr = it.next();
            if (tr.isUser != null && tr.isUser == 1) {
                logger.finest("Word exists: " + word.getName());
            } else {
                client.addWord(word.getName(), tr.value, word.getContext());
                logger.finest("Word added: " + word.getName());
            }
        }
    }

    private void updateProgress(double progress) {
        if(progressBar != null) {
            progressBar.setProgress(progress);
        }
    }
}
