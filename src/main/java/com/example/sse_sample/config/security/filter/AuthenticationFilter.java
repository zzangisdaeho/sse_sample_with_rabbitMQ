package com.example.sse_sample.config.security.filter;

import com.example.sse_sample.config.security.auth.AuthenticationProvider;
import com.example.sse_sample.config.security.auth.jwt.AccessToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationProvider authenticationProvider;

    private static final String[] PATTERNS = {"/test/*", "/notifications*"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        AccessToken accessToken = AccessToken.ofNullable(request);

        if (accessToken == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid access token");
        }

        if (accessToken.isGuestToken()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

        SecurityContextHolder.getContext().setAuthentication(authenticationProvider.authenticate(accessToken));


        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();

        if (PatternMatchUtils.simpleMatch(PATTERNS, uri)) {
            return true;
        }
        else if(uri.equals("/") || uri.equals("/index.html")){
            return true;
        }

        return false;
    }

    private String extractToken(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null) {
            return "";
        }

        return authorizationHeader.replaceFirst("Bearer ", "");
    }
}
