package com.example.sse_sample.config.security.auth;

import com.example.sse_sample.config.security.auth.jwt.AccessToken;
import com.example.sse_sample.config.security.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationProvider {

    public AuthenticationProvider() {
        log.info("AuthenticationProvider is created");
    }

    public Authentication authenticate(AccessToken accessToken) {
        // 기존 db 연동 전에는 access token에 있는 사용자 ID만 저장한다.
        User user = new User(accessToken.getLongId());

        return new UsernamePasswordAuthenticationToken(user, "", null);
    }
}
