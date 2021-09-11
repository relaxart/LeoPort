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
                String[] split_str = line.split(";", 2);
                if (split_str.length == 1) {
                    line = line.trim();
                    this.words.add(new Word(line.toLowerCase()));
                } else {
                    this.words.add(new Word(split_str[0].trim()));
                    this.translations.put(split_str[0].trim(), split_str[1].trim());
                }
            }
            fileReader.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return getWords();
    }
}
