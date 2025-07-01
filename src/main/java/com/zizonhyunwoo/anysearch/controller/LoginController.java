package com.zizonhyunwoo.anysearch.controller;

import com.zizonhyunwoo.anysearch.domain.UserInfo;
import com.zizonhyunwoo.anysearch.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final JwtUtil jwtUtil;
    @GetMapping
    public String login() {
        return jwtUtil.createJwt(
                new UserInfo("user","pass","email","nickname","ROLE_ADMIN"),
                "access");
    }
}
