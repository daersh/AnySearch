package com.zizonhyunwoo.anysearch.util;

import com.zizonhyunwoo.anysearch.domain.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtilTest {

    String token = null;

    class JwtUtil{
        String secret= "sadfasdfasdfasfdasafsdfadsfadsfadsfasdfadsfasdfdasdfsdsfs";
        SecretKey key = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );

    }

    @Test
    @DisplayName("jwt 토큰 생성")
    public void jwt_토큰_생성(){
        UserInfo userInfo = new UserInfo("name","password","email","nickname");
        JwtUtil jwtUtil = new JwtUtil();
         token = Jwts.builder()
                .claim("user_name", userInfo.getName())
                .claim("user_email", userInfo.getEmail())
                .claim("user_nickname", userInfo.getNickname())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(jwtUtil.key)
                .compact();
        Claims claims = Jwts
                .parser()
                .verifyWith(jwtUtil.key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Assertions.assertEquals(userInfo.getName(), claims.get("user_name"));
        Assertions.assertEquals(userInfo.getEmail(), claims.get("user_email"));
        Assertions.assertEquals(userInfo.getNickname(), claims.get("user_nickname"));
    }

}
