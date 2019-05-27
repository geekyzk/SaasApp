package com.em248.security.jwt;

import cn.hutool.json.JSONUtil;
import com.em248.configuration.ApplicationProperties;
import com.em248.security.CustomUserDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
public abstract class TokenProvider {

    protected final ApplicationProperties applicationProperties;

    protected final Key key;

    protected long tokenValidityInMilliseconds;

    protected long tokenValidityInMillisecondsForRememberMe;

    protected String AUTHORITIES_KEY = "auth";

    public TokenProvider(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        byte[] keyBytes;
        String secret = applicationProperties.getSecurity().getAuthentication().getJwt().getSecret();
        if (!StringUtils.isEmpty(secret)) {
            log.warn("Warning: the JWT key used is not Base64-encoded. " +
                    "We recommend using the `jhipster.security.authentication.jwt.base64-secret` key for optimum security.");
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        } else {
            log.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(applicationProperties.getSecurity().getAuthentication().getJwt().getBase64Secret());
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityInMilliseconds =
                1000 * applicationProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe =
                1000 * applicationProperties.getSecurity().getAuthentication().getJwt()
                        .getTokenValidityInSecondsForRememberMe();
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }
        ObjectMapper mapper = new ObjectMapper();
        CustomUserDetail jwtUser = null;
        if (authentication.getPrincipal() instanceof CustomUserDetail) {
            jwtUser = (CustomUserDetail) authentication.getPrincipal();
        } else {
            throw new IllegalArgumentException("创建Token异常");
        }

        TokenSubject tokenSubject = new TokenSubject();
        tokenSubject.setDataSourceConfigId(jwtUser.getDataSourceConfigId());
        tokenSubject.setUserId(jwtUser.getId());
        return Jwts.builder()
            .setSubject(JSONUtil.toJsonStr(tokenSubject))
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact();
    }

    public abstract Authentication getAuthentication(String token);

    public Collection<? extends GrantedAuthority> getAuthority(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
        return Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }

    @Data
    public static class TokenSubject {
        private String userId;
        private String dataSourceConfigId;
    }
}
