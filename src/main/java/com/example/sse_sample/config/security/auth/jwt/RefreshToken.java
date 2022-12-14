package com.example.sse_sample.config.security.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Data
@Slf4j
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken implements JwtToken {
  public static final String REDIS_PREFIX = "@ref:";
  public static final String COOKIE_NAME = "__PVRFT";

  private String issuer;
  private Long createdAt;
  private String divider;
  private String type;
  private String email;
  private String userId;
  private String accountId;
  private Long expiredAt;
  private String raw;
  private boolean autoSignIn;

  public static RefreshToken ofNullable(String raw, HttpServletRequest request) {
    Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElse(new Cookie[]{});

    Cookie cookie = Arrays.stream(cookies)
        .filter((c) -> c.getName().equals(COOKIE_NAME))
        .findFirst()
        .orElse(null);

    if (cookie != null) {
      log.info("found refresh token from cookie.");
      return RefreshToken.ofNullable(cookie.getValue());
    }

    if (raw != null) {
      log.info("found refresh token from request body.");
      return RefreshToken.ofNullable(raw);
    }

    return null;
  }


  public static RefreshToken ofNullable(String raw) {
    Claims claims;
    try {
      claims = Jwts.parserBuilder()
          .setSigningKey(REFRESH_TOKEN_KEY.getBytes())
          .build()
          .parseClaimsJws(raw)
          .getBody();
    } catch(Exception e) {
      return null;
    }
    return RefreshToken.of(raw, claims);
  }

  public RefreshToken(String divider, String type,
      String email, String userId, String accountId, boolean autoSignIn) {

    this.divider = divider;
    this.type = type;
    this.email = email;
    this.userId = userId;
    this.accountId = accountId;
    this.autoSignIn = autoSignIn;
  }

  public static RefreshToken of(String raw, Claims claims) {
    return RefreshToken.builder()
        .issuer(claims.get("issuer", String.class))
        .createdAt(claims.get("createdAt", Long.class))
        .divider(claims.get("divider", String.class))
        .type(claims.get("type", String.class))
        .email(claims.get("email", String.class))
        .userId(claims.get("userId", String.class))
        .accountId(claims.get("accountId", String.class))
        .expiredAt(claims.get("expiredAt", Long.class))
        .autoSignIn(claims.get("autoSignIn", Boolean.class))
        .raw(raw)
        .build();
  }

  public int getDurationInSecond() {
    return (int)((expiredAt - createdAt)/1000);
  }

  public Cookie toCookie() {
    return createCookie(raw, getDurationInSecond());
  }

  public boolean isGuestToken() {
    return "-1".equals(userId) || "100".equals(type);
  }

  public Long getExpiredAt() {
    return expiredAt;
  }

  @Override
  public String getCookieName() {
    return COOKIE_NAME;
  }

  public String getRedisKey() {
    return REDIS_PREFIX + "@" + divider + ":" + email;
  }

  public String getRedisSubKey() {
    return "@" + divider + ":" + email;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RefreshToken that = (RefreshToken) o;
    return autoSignIn == that.autoSignIn && Objects.equals(issuer,
        that.issuer) && Objects.equals(createdAt, that.createdAt)
        && Objects.equals(divider, that.divider)
        && Objects.equals(type, that.type) && Objects.equals(
        email, that.email) && Objects.equals(userId, that.userId)
        && Objects.equals(accountId, that.accountId)
        && Objects.equals(expiredAt, that.expiredAt)
        && Objects.equals(raw, that.raw);
  }

  @Override
  public int hashCode() {
    return Objects.hash(issuer, createdAt, divider, type, email, userId,
        accountId, expiredAt, raw, autoSignIn);
  }
}