package com.lingualeo.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

abstract public class BaseHandler {
    protected File file;
    protected List<Word> words = new ArrayList<>();

    public List<Word> getWords() {
        return words;
    }

    abstract public void read();

    public BaseHandler(File file) {
        this.file = file;
    }

    /*
    abstract public void clean();
        #!/usr/bin/env python
# -*- coding: utf-8 -*-
import config
import sqlite3

kindle_config = config.sources.get('kindle')
conn = sqlite3.connect(kindle_config)
conn.execute("delete from WORDS;")
conn.close()
conn = sqlite3.connect(kindle_config)
conn = conn.execute("delete from LOOKUPS;")
conn.close()
conn = sqlite3.connect(kindle_config)
conn.execute("update metadata set sscnt = 0 where id in ('WORDS', 'LOOKUPS');")
conn.close()
    */
}
