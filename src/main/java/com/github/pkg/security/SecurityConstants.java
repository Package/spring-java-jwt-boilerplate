package com.github.pkg.security;

public class SecurityConstants {
    public static final String AUTH_REGISTER_URL = "/api/v1/auth/register";
    public static final String AUTH_LOGIN_URL = "/api/v1/auth/login";
    public static final String[] UNAUTHENTICATED_PAGES = {"/"};
    public static final int EXPIRATION_TIME_SECS = 1000 * 60 * 60 * 10;
    public static final String SECRET_KEY = "7FPwz8J3UoZSOKO9sSkC7g0xq7osqs2zpWqNvqQPajsw";
}
