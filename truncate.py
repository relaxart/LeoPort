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