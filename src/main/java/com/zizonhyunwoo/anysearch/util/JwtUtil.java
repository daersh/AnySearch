package com.zizonhyunwoo.anysearch.util;

import com.zizonhyunwoo.anysearch.domain.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    SecretKey key;
    @Value("${jwt.key}")
    String jwtKey;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtKey.getBytes());
    }

    public String createJwt(UserInfo userInfo, String type){
        return Jwts.builder()
                .claim("user_name", userInfo.getName())
                .claim("user_email", userInfo.getEmail())
                .claim("user_nickname", userInfo.getNickname())
                .claim("user_role", userInfo.getRole())
                .claim("type", type)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000000))
                .signWith(key)
                .compact();
    }

    public Claims parseJwt(String jwt){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    public String parseJwt(String jwt, String field){
        return parseJwt(jwt).get(field, String.class);
    }

    public Boolean isExpired(String jwt){
        return parseJwt(jwt).getExpiration()
                .before(new Date(System.currentTimeMillis()));
    }

}
