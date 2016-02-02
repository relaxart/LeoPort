import sqlite3

class Word:
    text = '';
    context = '';

    def __init__(self, text):
        self.text = text

class Base(object):
    data = []

    def __init__(self, source):
        self.source = source

    def get(self):
        return self.data

    def read(self):
        raise NotImplementedError('Not implemented yet')


class Kindle(Base):
    def read(self):
        conn = sqlite3.connect(self.source)
        sql = 'select word, usage from words LEFT JOIN LOOKUPS ON words.id = LOOKUPS.word_key where words.lang="en" GROUP BY word ORDER BY word;'
        for row in conn.execute(sql):
            if isinstance(row[0], unicode):
                word = Word(row[0])
                if isinstance(row[1], unicode):
                    word.context = row[1]
                self.data.append(word)
        conn.close()


class Text(Base):
    def read(self):
        f = open(self.source)
        for word in f.readlines():
            self.data.append(Word(word))
        f.close()
