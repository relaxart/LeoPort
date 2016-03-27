package com.lingualeo.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

class TextReader extends BaseReader {
    TextReader(File file) {
        super(file);
    }

    @Override
    public List<Word> read() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.file));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                this.words.add(new Word(line.toLowerCase()));
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return getWords();
    }
}
