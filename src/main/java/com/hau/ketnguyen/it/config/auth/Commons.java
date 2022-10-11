package com.hau.ketnguyen.it.config.auth;

import org.springframework.security.core.userdetails.User;

public class Commons {
    public static final String LOGIN_URL = "/login";
    public static final String REFRESH_TOKEN = "/refresh-token";
    public static final String ACTIVE_URL = "/activation";
    public static final String SIGN_UP_URL = "/sign-up";
    public static final String AUTH_HEADER = "Authorization";
    public static final String Refresh_Token = "RefreshToken";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_SECRET = "Siten@AllRights";
    public static final String TOKEN_ISSUER = "http://siten.vn/";
    public static final String DEFAULT_ROLE = "DEFAULT";

    public static final String[] PUBLIC_URLs = new String[]{LOGIN_URL, REFRESH_TOKEN, SIGN_UP_URL, ACTIVE_URL, "/"};

    public static String jwtSubject(User user) {
        return user.getUsername();
    }

    public static String principal(String subject) {
        return subject;
    }

}
