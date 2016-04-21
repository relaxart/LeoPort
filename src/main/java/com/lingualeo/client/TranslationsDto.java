package com.lingualeo.client;

import java.util.ArrayList;
import java.util.List;

public class TranslationsDto {
    public String error_msg;
    public Integer is_user;
    public List<TranslateDto> translate = new ArrayList<>();
    public String transcription;

}
