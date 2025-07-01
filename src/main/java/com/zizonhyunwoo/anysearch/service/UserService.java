package com.zizonhyunwoo.anysearch.service;

import com.zizonhyunwoo.anysearch.domain.UserInfo;

import java.util.List;

public interface UserService {

    UserInfo getUserByuserNickname(String userNickname);
    List<UserInfo> getUserList(Integer pageNo, Integer pageSize);
    UserInfo saveUser(UserInfo userInfo);
    UserInfo updateUser(UserInfo userInfo);
    Boolean deleteUser(String userNickname);
}
