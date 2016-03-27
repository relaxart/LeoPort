package com.lingualeo.reader;

public class Word {
    private String name;
    private String context;

    Word(String name) {
        this.name = name;
    }

    void setContext(String context) {
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public String getContext() {
        return context;
    }
}
