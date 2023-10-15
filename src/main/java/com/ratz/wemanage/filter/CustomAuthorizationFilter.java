package com.ratz.wemanage.filter;

import com.ratz.wemanage.provider.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String EMPTY = "";
    public static final String HTTP_OPTIONS_METHOD = "OPTIONS";
    private static final String TOKEN_KEY = "token";
    private static final String EMAIL_KEY = "email";
    private static final String[] PUBLIC_ROUTES = {"/api/v1/user/login", "/api/v1/user/verify/code", "/api/v1/user/register"};

    
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            Map<String, String> values = getRequestValues(request);
            String token = getToken(request);

            if (tokenProvider.isTokenValid(values.get(EMAIL_KEY), token)) {
                List<GrantedAuthority> authorities = tokenProvider.getAuthorities(values.get(TOKEN_KEY));
                Authentication authentication = tokenProvider.getAuthentication(values.get(EMAIL_KEY), authorities, request);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else {
                SecurityContextHolder.clearContext();
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getHeader(AUTHORIZATION) == null || !request.getHeader(AUTHORIZATION).startsWith(BEARER)
                || request.getMethod().equalsIgnoreCase(HTTP_OPTIONS_METHOD)
                || asList(PUBLIC_ROUTES).contains(request.getRequestURI());
    }

    private Map<String, String> getRequestValues(HttpServletRequest request) {
        return Map.of(EMAIL_KEY, tokenProvider.getSubject(getToken(request), request),
                TOKEN_KEY, getToken(request));
    }

    private String getToken(HttpServletRequest request) {
        return ofNullable(request.getHeader(AUTHORIZATION)).filter(h -> h.startsWith(BEARER))
                .map(h -> h.replace(BEARER, EMPTY)).get();
    }

}
