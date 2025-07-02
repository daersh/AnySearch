package com.zizonhyunwoo.anysearch.controller;

import com.zizonhyunwoo.anysearch.domain.UserInfo;
import com.zizonhyunwoo.anysearch.request.LoginRequest;
import com.zizonhyunwoo.anysearch.service.UserService;
import com.zizonhyunwoo.anysearch.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @GetMapping("/sample")
    public String loginSample() {
        return jwtUtil.createJwt(
                new UserInfo("user","pass","email","nickname","ROLE_ADMIN"),
                "access");
    }
    @PostMapping
    public String login(@Valid @RequestBody LoginRequest userInfo, HttpServletResponse response) {
        return userService.login(userInfo,response);
    }
}
