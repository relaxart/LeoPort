[![Build Status](https://travis-ci.org/relaxart/LeoPort.svg?branch=master)](https://travis-ci.org/relaxart/LeoPort)
[![Coverage Status](https://coveralls.io/repos/github/relaxart/LeoPort/badge.svg?branch=test_prepare)](https://coveralls.io/github/relaxart/LeoPort?branch=master)

LeoPort
=======
Allow add words from several formats to [Lingualeo.com](http://lingualeo.com/)

## Requirements
Java 13
[Oracle Java](https://www.oracle.com/technetwork/java/javase/downloads/jdk13-downloads-5672538.html)

## How to launch on Windows
```
gradlew.bat run
```

## How to launch on MacOs/Unix
```
./gradlew run
```

## Formats
- Kindle (sqlite) Default file located /Volumes/Kindle/system/vocabulary/vocab.db
- Text. All words should be in one column.

## Text example:
apple  
orange  
watermelon
