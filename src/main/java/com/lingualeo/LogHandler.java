package com.lingualeo;

import javafx.scene.control.TextArea;

import java.util.logging.Handler;
import java.util.logging.LogRecord;


class LogHandler extends Handler {
    private TextArea textArea;

    LogHandler(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void publish(LogRecord record) {
        textArea.appendText(record.getMessage() + "\n");
    }

    @Override
    public void flush() {
        textArea.clear();
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }
}
