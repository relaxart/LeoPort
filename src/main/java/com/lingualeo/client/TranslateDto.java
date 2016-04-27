package com.lingualeo.client;

import com.google.gson.annotations.SerializedName;

public class TranslateDto {
    public Integer id;
    public String value;
    public Integer votes;
    @SerializedName("is_user")
    public Integer isUser;
}
