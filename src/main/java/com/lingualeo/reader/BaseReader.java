package com.lingualeo.reader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseReader {
    File file;
    List<Word> words = new ArrayList<>();

    BaseReader(File file) {
        this.file = file;
    }

    List<Word> getWords() {
        return words;
    }

    public abstract List<Word> read();
}
