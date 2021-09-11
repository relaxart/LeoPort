package com.lingualeo.reader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class BaseReader {
    File file;
    List<Word> words = new ArrayList<>();
    HashMap<String, String> translations = new HashMap<>();

    BaseReader(File file) {
        this.file = file;
    }

    List<Word> getWords() {
        return words;
    }
    public HashMap<String, String> getTranslations() { return translations; }

    public abstract List<Word> read();
}
