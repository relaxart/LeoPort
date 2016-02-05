#!/usr/bin/env python
# -*- coding: utf-8 -*-
import config
import sqlite3

kindle_config = config.sources.get('kindle')

conn = sqlite3.connect(kindle_config)
cursor = conn.cursor()
cursor.execute("delete from WORDS;")
conn.commit()
cursor.close()

conn = sqlite3.connect(kindle_config)
cursor = conn.cursor()
cursor.execute("delete from LOOKUPS;")
conn.commit()
cursor.close()

conn = sqlite3.connect(kindle_config)
cursor = conn.cursor()
cursor.execute("update metadata set sscnt = 0 where id in ('WORDS', 'LOOKUPS');")
conn.commit()
cursor.close()
