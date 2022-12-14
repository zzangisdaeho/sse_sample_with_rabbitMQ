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

@Slf4j
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
public class AccessToken implements JwtToken {
    public static final String REDIS_PREFIX = "@acc:";
    public static final String COOKIE_NAME = "__PVACT";

    private String issuer;
    private Long createdAt;
    private String divider;
    private String type;
    private String email;
    private String userId;
    private String investorId;
    private String advisorId;
    private String name;
    private String alias;
    private Long expiredAt;
    private String raw;

    public AccessToken(String divider, String type,
        String email, String userId,
        String investorId, String advisorId) {

        this.divider = divider;
        this.type = type;
        this.email = email;
        this.userId = userId;
        this.investorId = investorId;
        this.advisorId = advisorId;
        this.name = "";
        this.alias = "";
    }

    public static AccessToken ofNullable(HttpServletRequest request) {
        Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElse(new Cookie[]{});

        Cookie cookie = Arrays.stream(cookies)
            .filter((c) -> c.getName().equals(COOKIE_NAME))
            .findFirst()
            .orElse(null);

        if (cookie != null) {
            log.info("found access token from cookie.");
            return AccessToken.ofNullable(cookie.getValue());
        }

        String header = request.getHeader("Authorization");
        if (header == null) {
            return null;
        }

        log.info("found access token from header.");
        return AccessToken.ofNullable(header.replaceFirst("Bearer ", ""));
    }

    public static AccessToken ofGuest() {
        return AccessToken.builder()
                .issuer("")
                .createdAt(1L)
                .divider("")
                .type("")
                .email("")
                .userId("-1")
                .investorId("")
                .advisorId("")
                .name("")
                .alias("")
                .expiredAt(2L)
                .raw("")
                .build();
    }

    public static AccessToken ofNullable(String raw) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                .setSigningKey(GENERAL_TOKEN_KEY.getBytes())
                .build()
                .parseClaimsJws(raw)
                .getBody();
        } catch(Exception e) {
            return null;
        }

        return AccessToken.of(raw, claims);
    }

    public static AccessToken of(String raw, Claims claims) {
        return AccessToken.builder()
            .issuer(claims.get("issuer", String.class))
            .createdAt(claims.get("createdAt", Long.class))
            .divider(claims.get("divider", String.class))
            .type(claims.get("type", String.class))
            .email(claims.get("email", String.class))
            .userId(claims.get("userId", String.class))
            .investorId(claims.get("investorId", String.class))
            .advisorId(claims.get("advisorId", String.class))
            .name(claims.get("name", String.class))
            .alias(claims.get("alias", String.class))
            .expiredAt(claims.get("expiredAt", Long.class))
            .raw(raw)
            .build();
    }

    public Long getLongId() {
        return Long.parseLong(userId);
    }
    public String getRedisKey() {
        return REDIS_PREFIX + "@" + divider + ":" + email;
    }

    public String getRedisSubKey() {
        return "@" + divider + ":" + email;
    }

    public int getDurationInSecond() {
        return (int)((expiredAt - createdAt)/1000);
    }

    public Cookie toCookie() {
        return createCookie(raw, getDurationInSecond());
    }

    public Long getExpiredAt() {
        return expiredAt;
    }

    @Override
    public String getCookieName() {
        return COOKIE_NAME;
    }

    public boolean isGuestToken() {
        return "-1".equals(userId) || "100".equals(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccessToken that = (AccessToken) o;
        return Objects.equals(issuer, that.issuer)
            && Objects.equals(createdAt, that.createdAt)
            && Objects.equals(divider, that.divider)
            && Objects.equals(type, that.type) && Objects.equals(
            email, that.email) && Objects.equals(userId, that.userId)
            && Objects.equals(investorId, that.investorId)
            && Objects.equals(advisorId, that.advisorId)
            && Objects.equals(name, that.name) && Objects.equals(
            alias, that.alias) && Objects.equals(expiredAt, that.expiredAt)
            && Objects.equals(raw, that.raw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issuer, createdAt, divider, type, email, userId,
            investorId, advisorId, name, alias, expiredAt, raw);
    }
}
