#!/usr/bin/env python
# -*- coding: utf-8 -*-
import handler
import config
import service
import sys

email = config.auth.get('email')
password = config.auth.get('password')

export_type = sys.argv[1]

if export_type == 'text':
    word_handler = handler.Text(config.sources.get('text'))
elif export_type == 'kindle':
    word_handler = handler.Kindle(config.sources.get('kindle'))
else:
    raise Exception('unsupported type')

word_handler.read()

lingualeo = service.Lingualeo(email, password)
lingualeo.auth()

for word_dto in word_handler.get():
    word = word_dto.text.lower().encode('utf-8')
    translate = lingualeo.get_translates(word)

    if translate["is_exist"]:
        print "Already exists: " + word.strip()
    else:
        context = word_dto.context.encode('utf-8')
        lingualeo.add_word(word, translate["tword"], context)
        print "Add word: " + word.strip()