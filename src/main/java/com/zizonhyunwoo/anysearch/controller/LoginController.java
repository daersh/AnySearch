package com.zizonhyunwoo.anysearch.controller;

import com.zizonhyunwoo.anysearch.domain.UserInfo;
import com.zizonhyunwoo.anysearch.request.LoginRequest;
import com.zizonhyunwoo.anysearch.service.UserService;
import com.zizonhyunwoo.anysearch.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
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
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest userInfo, HttpServletResponse response) {
        return ResponseEntity.ok(userService.login(userInfo,response));
    }
    @PostMapping("/register")
    public ResponseEntity<UserInfo> addUser(@RequestBody UserInfo user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

}
