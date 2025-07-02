package com.zizonhyunwoo.anysearch.controller;

import com.zizonhyunwoo.anysearch.domain.UserInfo;
import com.zizonhyunwoo.anysearch.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<UserInfo> getUserInfo(@AuthenticationPrincipal UserInfo userInfo) {
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("")
    public ResponseEntity<UserInfo> getUser(@RequestParam String userNickname) {
        return ResponseEntity.ok(userService.getUserByuserNickname(userNickname));
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserInfo>> getUsers(@RequestParam Integer page, @RequestParam Integer size) {
        return ResponseEntity.ok(userService.getUserList(page, size));
    }

    @PutMapping("")
    public ResponseEntity<UserInfo> updateUser(@RequestBody UserInfo user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping("")
    public ResponseEntity<Boolean> deleteUser(@RequestBody String userNickname) {
        return ResponseEntity.ok(userService.deleteUser(userNickname));
    }

}
