package com.lingualeo.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class TextReader extends BaseReader {
    private static final Logger logger = Logger.getLogger(TextReader.class.getName());

    TextReader(File file) {
        super(file);
    }

    @Override
    public List<Word> read() {
        try {
            FileReader fileReader = new FileReader(this.file);
            BufferedReader br = new BufferedReader(fileReader);
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                this.words.add(new Word(line.toLowerCase()));
            }
            fileReader.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return getWords();
    }
}
