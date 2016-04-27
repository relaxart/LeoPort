package com.lingualeo.reader;

import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class KindleReader extends BaseReader {
    private static final Logger logger = Logger.getLogger(KindleReader.class.getName());

    KindleReader(File file) {
        super(file);
    }

    @Override
    public List<Word> read() {
        try {
            SQLiteJDBCLoader.initialize();
            SQLiteDataSource dataSource = new SQLiteDataSource();
            dataSource.setUrl("jdbc:sqlite:" + this.file.getAbsoluteFile().toString());

            Connection c = dataSource.getConnection();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select word, usage from words " +
                    "LEFT JOIN LOOKUPS ON words.id = LOOKUPS.word_key " +
                    "WHERE words.lang=\"en\" GROUP BY word ORDER BY word;");
            while (rs.next()) {
                Word w = new Word(rs.getString("word").toLowerCase());
                w.setContext(rs.getString("usage"));
                this.words.add(w);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return getWords();
    }
}
