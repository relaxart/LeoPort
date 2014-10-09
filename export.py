import word, config, service, sys

email = config.auth.get('email')
password = config.auth.get('password')

try:
    type = sys.argv[1]
    if type == 'text':
        handler = word.Text(config.sources.get('text'))
    elif type == 'kindle':
        handler = word.Kindle(config.sources.get('kindle'))
    else:
        raise Exception('unsupported type')

    handler.read()

    lingualeo = service.Lingualeo(email, password)
    lingualeo.auth()

    for word in handler.get():
        word = word.lower();
	translate = lingualeo.getTranslates(word)

        lingualeo.addWord(translate["word"], translate["tword"])

        if translate["is_exist"]:
            result = "Add word: "
        else:
            result = "Alredy exist: "

        result = result + word
        print result


except Exception as e:
    print e
