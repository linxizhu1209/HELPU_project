package com.github.backend.web.entity.enums;

import static java.util.Locale.ENGLISH;

public enum OauthLoginType {
    KAKAO,
    NAVER,
    GOOGLE;

    public static OauthLoginType fromName(String type) {
      return OauthLoginType.valueOf(type.toUpperCase(ENGLISH));
    }
}
