package com.lingualeo.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import javax.naming.AuthenticationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ApiClient {
    private final static String API_URL = "http://api.lingualeo.com/";
    private final String login;
    private final String password;
    private boolean isAuthed = false;
    private final Gson gson = new GsonBuilder().create();

    public ApiClient(String login, String password) {
        this.login = login;
        this.password = password;
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    }

    public void auth() throws AuthenticationException, IOException {
        String urlParameters = "email=" + this.login + "&password=" + this.password;
        String requestUrl = API_URL + "api/login";

        HttpURLConnection conn = getHttpURLConnection(requestUrl, "POST", urlParameters);
        JsonObject gsonObject = gson.fromJson(processResponse(conn), JsonObject.class);
        String errorMsg = gsonObject.get("error_msg").getAsString();

        if (errorMsg.length() > 0) {
            this.isAuthed = false;
            throw new AuthenticationException(errorMsg);
        } else {
            this.isAuthed = true;
        }
    }

    public List<TranslateDto> getTranslates(String word) throws AuthenticationException, IOException {
        checkUserRights();
        String urlParameters = "word=" + word;
        String requestUrl = API_URL + "gettranslates";

        HttpURLConnection conn = getHttpURLConnection(requestUrl, "GET", urlParameters);
        TranslationsDto translation = gson.fromJson(processResponse(conn), TranslationsDto.class);

        return translation.translate;
    }

    public void addWord(String word, String translate, String context) throws AuthenticationException, IOException {
        checkUserRights();
        String urlParameters = "word=" + word + "&tword=" + translate + "&context=" + context;
        String requestUrl = API_URL + "addword";

        HttpURLConnection conn = getHttpURLConnection(requestUrl, "POST", urlParameters);
        processResponse(conn);
    }

    private HttpURLConnection getHttpURLConnection(String requestUrl, String method, String urlParameters) throws IOException {
        int postDataLength = getPostDataLength(urlParameters);
        URL url = new URL(requestUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestProperty("charset", "utf-8");

        switch (method) {
            case "POST":
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                break;
            case "GET":
                conn.setRequestMethod("GET");
                break;
        }

        conn.setUseCaches(false);
        if (urlParameters != null) {
            conn.getOutputStream().write(urlParameters.getBytes());
        }

        return conn;
    }

    private int getPostDataLength(String urlParameters) {
        if (urlParameters != null) {
            byte[] postData = urlParameters.getBytes();
            return postData.length;
        } else {
            return 0;
        }
    }

    private String processResponse(HttpURLConnection conn) throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        BufferedReader br = new BufferedReader(reader);
        String line;
        String responseBody = br.readLine();
        while ((line = br.readLine()) != null) {
            responseBody = responseBody + line;
        }

        assert responseBody != null;
        return responseBody;
    }


    private void checkUserRights() throws AuthenticationException {
        if (!this.isAuthed) {
            throw new AuthenticationException("User doesn't have credentials.");
        }
    }

}
