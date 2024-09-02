package org.chzzk.howmeet.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class TokenProvider {
    private final Long expiration;
    private final Key key;
    private final ObjectMapper objectMapper;

    public TokenProvider(@Value("${auth.access-token.expiration}") final Long expiration,
                         final ObjectMapper objectMapper,
                         @Value("${auth.access-token.secret-key}") final String secretKey) {
        this.expiration = expiration;
        this.objectMapper = objectMapper;
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(final AuthPrincipal authPrincipal) {
        final String payload = getPrincipalPayload(authPrincipal);
        final Claims claims = Jwts.claims()
                .setSubject(payload);
        final Date now = new Date();
        final Date expirationDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public String getPayload(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(final String token) {
        try {
            final Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            log.info("JWT 검증 실패 : {}", e.getMessage());
            return false;
        }
    }

    private String getPrincipalPayload(final AuthPrincipal authPrincipal) {
        try {
            return objectMapper.writeValueAsString(authPrincipal);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException();
        }
    }
}
