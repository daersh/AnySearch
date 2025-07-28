package com.zizonhyunwoo.anysearch.user.service;

import com.zizonhyunwoo.anysearch.user.domain.UserInfo;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface UserService {

    UserInfo getUserByuserNickname(String userNickname);
    List<UserInfo> getUserList(Integer pageNo, Integer pageSize);
    UserInfo saveUser(UserInfo userInfo);
    UserInfo updateUser(UserInfo userInfo);
    Boolean deleteUser(String userNickname);

    String login(LoginRequest req, HttpServletResponse response);
}
