package com.lingualeo;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class TextAreaOutputStream extends OutputStream {
    private final JTextArea textArea;
    private final StringBuilder buffer = new StringBuilder(10);
    private final PrintStream old;

    public TextAreaOutputStream(final JTextArea textArea, PrintStream old) {
        this.textArea = textArea;
        this.old = old;
        buffer.append("> ");
    }

    @Override
    public void write(int b) throws IOException {
        char c = (char) b;
        String value = Character.toString(c);
        buffer.append(value);
        if (value.equals("\n")) {
            textArea.append(buffer.toString());
            buffer.delete(0, buffer.length());
            buffer.append("> ");
        }
        old.print(c);
    }
}
