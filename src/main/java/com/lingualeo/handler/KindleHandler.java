package com.lingualeo.handler;

import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class KindleHandler extends BaseHandler {

    public KindleHandler(File file) {
        super(file);
    }

    @Override
    public void read() {
        try {
            SQLiteJDBCLoader.initialize();
            SQLiteDataSource dataSource = new SQLiteDataSource();
            dataSource.setUrl("jdbc:sqlite:" + this.file.getAbsoluteFile().toString());

            Connection c = dataSource.getConnection();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select word, usage from words LEFT JOIN LOOKUPS ON words.id = LOOKUPS.word_key where words.lang=\"en\" GROUP BY word ORDER BY word;");
            while (rs.next()) {
                Word w = new Word(rs.getString("word").toLowerCase());
                w.setContext(rs.getString("usage"));
                this.words.add(w);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
