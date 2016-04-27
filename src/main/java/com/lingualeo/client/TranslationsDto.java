package com.lingualeo.client;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TranslationsDto {
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("is_user")
    public Integer isUser;
    public List<TranslateDto> translate = new ArrayList<>();
    public String transcription;

}
