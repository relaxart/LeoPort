package com.lingualeo.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApiClient {
    private static final String API_URL = "https://api.lingualeo.com/";
    private final String login;
    private final String password;
    private boolean isAuthed = false;
    private final Gson gson = new GsonBuilder().create();
    private static final Logger logger = Logger.getLogger(ApiClient.class.getName());

    public ApiClient(String login, String password) {
        this.login = login;
        this.password = password;
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    }

    public void auth() throws AuthenticationException {
        String urlParameters = "email=" + this.login + "&password=" + this.password;
        String requestUrl = API_URL + "login";
        JsonElement errorMsg;

        try {
            HttpURLConnection conn = getHttpURLConnection(requestUrl, "POST", urlParameters);
            String response = processResponse(conn);
            JsonObject gsonObject = gson.fromJson(response, JsonObject.class);
            errorMsg = gsonObject.get("error_msg");
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            throw new AuthenticationException(e.getMessage());
        }

        this.isAuthed = true;
        if (errorMsg != null) {
            this.isAuthed = false;
            throw new AuthenticationException(errorMsg.getAsString());
        }
    }

    public List<TranslateDto> getTranslates(String word) throws AuthenticationException {
        checkUserRights();
        String urlParameters = "word=" + word;
        String requestUrl = API_URL + "gettranslates";
        TranslationsDto translation;
        try {
            HttpURLConnection conn = getHttpURLConnection(requestUrl, "GET", urlParameters);
            translation = gson.fromJson(processResponse(conn), TranslationsDto.class);
        } catch (IOException e) {
            translation = new TranslationsDto();
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return translation.translate;
    }

    public void addWord(String word, String translate, String context) throws AuthenticationException {
        checkUserRights();
        logger.fine(translate);
        String urlParameters = "word=" + word + "&tword=" + URLEncoder.encode(translate, StandardCharsets.UTF_8) + "&context=" + context;
        String requestUrl = API_URL + "addword";

        try {
            HttpURLConnection conn = getHttpURLConnection(requestUrl, "POST", urlParameters);
            processResponse(conn);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    private HttpURLConnection getHttpURLConnection(String requestUrl, String method, String urlParameters) throws IOException {
        int postDataLength = getPostDataLength(urlParameters);
        URL url = new URL(requestUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestProperty("charset", "utf-8");
        conn.setUseCaches(false);

        if ("POST".equals(method)) {
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        }

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
        Reader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        BufferedReader br = new BufferedReader(reader);
        String line;
        StringBuilder responseBody = new StringBuilder(br.readLine());
        while ((line = br.readLine()) != null) {
            responseBody.append(line);
        }

        return responseBody.toString();
    }

    private void checkUserRights() throws AuthenticationException {
        if (!this.isAuthed) {
            throw new AuthenticationException("User doesn't have credentials.");
        }
    }
}
