import urllib, urllib2, json
from cookielib import CookieJar

class Lingualeo:
    def __init__(self, email, password):
        self.email = email
        self.password = password
        self.cj = CookieJar()

    def auth(self):
        url = "http://api.lingualeo.com/api/login"
        values = {
            "email" : self.email,
            "password" : self.password
        }

        return self.getContent(url, values)

    def addWord(self, word, tword):
        url = "http://api.lingualeo.com/addword"
        values = {
            "word" : word,
            "tword" : tword
        }
        self.getContent(url, values)

    def getTranslates(self, word):
        url = "http://api.lingualeo.com/gettranslates?word=" + word

        try:
            result = self.getContent(url, {})
            translate = result["translate"][0]
            return {
                "is_exist" : translate["is_user"],
                "word" : word,
                "tword" : translate["value"].encode("utf-8")
            }
        except Exception as e:
            return False

    def getContent(self, url, values):
        data = urllib.urlencode(values)

        opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(self.cj))
        req = opener.open(url, data)

        return json.loads(req.read())
