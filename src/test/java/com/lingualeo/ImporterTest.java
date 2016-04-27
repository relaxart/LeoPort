package com.lingualeo;

import com.lingualeo.client.ApiClient;
import com.lingualeo.client.TranslateDto;
import com.lingualeo.reader.Word;
import org.junit.Assert;
import org.junit.Test;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImporterTest extends Assert {

    @Test
    public void testStartImportOk() throws IOException, AuthenticationException {
        ApiClient clientMock = mock(ApiClient.class);

        List<TranslateDto> translates = new ArrayList<>();
        TranslateDto translate = new TranslateDto();
        translate.value = "тест";
        translate.isUser = 0;
        translates.add(translate);

        when(clientMock.getTranslates("test")).thenReturn(translates);

        List<Word> words = new ArrayList<>();
        words.add(new Word("test"));
        Importer im = new Importer(words, clientMock);
        im.startImport();
    }

    @Test
    public void testStartImportWithRussianWords() throws IOException, AuthenticationException {
        ApiClient clientMock = mock(ApiClient.class);
        List<TranslateDto> translates = new ArrayList<>();

        when(clientMock.getTranslates("русское слово")).thenReturn(translates);

        List<Word> words = new ArrayList<>();
        words.add(new Word("test"));
        Importer im = new Importer(words, clientMock);
        im.startImport();
    }
}
