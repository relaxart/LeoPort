package com.lingualeo.reader;

import java.io.File;

public class ReaderFactory {
    public static BaseReader create(File file) {
        String fileName = file.getName();
        int i = fileName.lastIndexOf('.') + 1;
        return "db".equals(fileName.substring(i)) ? new KindleReader(file) : new TextReader(file);
    }
}
