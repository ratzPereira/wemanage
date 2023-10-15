package com.ratz.wemanage.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ratz.wemanage.domain.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static java.time.LocalTime.now;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Component
public class TokenProvider {

    public static final String WE_MANAGE = "wemanage";
    public static final String AUTHORITIES = "authorities";
    public static final int ACCESS_TOKEN_HOURS_TO_ADD = 6;
    public static final int REFRESH_TOKEN_HOURS_TO_ADD = 96;


    @Value("${jwt.secret}")
    private String secret;

    public String createAccessToken(UserPrincipal userPrincipal) {

        String[] claims = getClaimsFromUser(userPrincipal);
        return JWT.create()
                .withIssuer(WE_MANAGE)
                .withAudience(WE_MANAGE)
                .withIssuedAt(Instant.from(now()))
                .withSubject(userPrincipal.getUsername())
                .withArrayClaim(AUTHORITIES, claims)
                .withExpiresAt(Instant.from(now().plusHours(ACCESS_TOKEN_HOURS_TO_ADD)))
                .sign(HMAC512(secret.getBytes()));
    }

    public String createRefreshToken(UserPrincipal userPrincipal) {

        String[] claims = getClaimsFromUser(userPrincipal);
        return JWT.create()
                .withIssuer(WE_MANAGE)
                .withAudience(WE_MANAGE)
                .withIssuedAt(Instant.from(now()))
                .withSubject(userPrincipal.getUsername())
                .withExpiresAt(Instant.from(now().plusHours(REFRESH_TOKEN_HOURS_TO_ADD)))
                .sign(HMAC512(secret.getBytes()));
    }


    public List<GrantedAuthority> getAuthorities(String token) {
        String[] claims = getClaimsFromToken(token);
        return stream(claims)
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }

    private String[] getClaimsFromToken(String token) {

        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }

    private JWTVerifier getJWTVerifier() {

        JWTVerifier verifier;

        try {
            Algorithm algorithm = HMAC512(secret);
            verifier = JWT.require(algorithm).withIssuer(WE_MANAGE).build();

        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Token cannot be verified");
        }

        return verifier;
    }

    private String[] getClaimsFromUser(UserPrincipal userPrincipal) {

        return userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
    }
}
