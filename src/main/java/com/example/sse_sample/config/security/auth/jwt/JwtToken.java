package com.example.sse_sample.config.security.auth.jwt;


import jakarta.servlet.http.Cookie;

public interface JwtToken {

  public final static String GENERAL_TOKEN_KEY = "3342f8da-fe24-41ec-b939-0242ac120123";
  public final static String REFRESH_TOKEN_KEY = "794567e4-e371-a72c-10f9-a8e29f729b02";

  String getRedisKey();
  String getRedisSubKey();
  String getRaw();
  String getDivider();
  String getEmail();
  Cookie toCookie();
  Long getExpiredAt();
  String getCookieName();

  default Cookie createCookie(String value, int maxAge) {
    Cookie cookie = new Cookie(getCookieName(), value);
    cookie.setMaxAge(maxAge);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setSecure(GlobalConstant.cookieSecure);
    cookie.setDomain(GlobalConstant.cookieDomain);
    return cookie;
  }
}
