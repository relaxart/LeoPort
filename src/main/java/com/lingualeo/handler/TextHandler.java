package com.lingualeo.handler;

import java.io.*;

public class TextHandler extends BaseHandler {
    public TextHandler(File file) {
        super(file);
    }

    @Override
    public void read() {
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
    }
}
