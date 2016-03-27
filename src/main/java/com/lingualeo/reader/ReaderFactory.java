package com.lingualeo.reader;

import java.io.File;

public class ReaderFactory {
    public static BaseReader create(File file) {
        String fileName = file.getName();
        int i = fileName.lastIndexOf('.');
        BaseReader reader;
        switch (fileName.substring(i + 1)) {
            case "db":
                reader = new KindleReader(file);
                break;
            default:
                reader = new TextReader(file);
                break;
        }

        return reader;
    }
}
