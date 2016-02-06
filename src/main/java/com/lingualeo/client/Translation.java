package com.lingualeo.client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Translation {
    private boolean isUser;
    private int wordId;
    private List<Translate> translates = new ArrayList<>();

    public Translation(JSONObject jsonObject) {
        this.isUser = jsonObject.getInt("is_user") == 1;
        this.wordId = jsonObject.getInt("word_id");
        JSONArray translateArray = jsonObject.getJSONArray("translate");
        for (int i = 0; i < translateArray.length(); i++)
        {
            JSONObject translateJson = translateArray.getJSONObject(i);
            Translate translate = new Translate(
                    translateJson.getInt("votes"),
                    translateJson.getInt("id"),
                    translateJson.getString("pic_url"),
                    translateJson.getString("value"),
                    translateJson.getInt("is_user") == 1
            );
            this.translates.add(translate);

        }
    }

    public List<Translate> getTranslates() {
        return translates;
    }

    public boolean isUser() {
        return isUser;
    }

    public int getWordId() {
        return wordId;
    }
}

