package com.zizonhyunwoo.anysearch.util;

import com.zizonhyunwoo.anysearch.user.domain.UserInfo;
import com.zizonhyunwoo.anysearch.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtFilterTest {
    String token;

    @Autowired
    JwtUtil jwtUtil;

    UserInfo userInfo = new UserInfo("name","password","email","nickname","ROLE_USER");

    @BeforeEach
    public void init(){
        token = jwtUtil.createJwt(userInfo,"access");
    }

    @Test
    @DisplayName("jwt 토큰 필터 통과")
    public void jwt_필터_통과(){

        assertNotNull(token);
        Claims claims = jwtUtil.parseJwt(token);

        assertFalse(claims.getExpiration().before(new Date(System.currentTimeMillis())));
        assertEquals("access", claims.get("type", String.class));

    }



}
