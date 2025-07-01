package com.zizonhyunwoo.anysearch.service.impl;

import com.zizonhyunwoo.anysearch.dao.UserDao;
import com.zizonhyunwoo.anysearch.domain.UserInfo;
import com.zizonhyunwoo.anysearch.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public UserInfo getUserByuserNickname(String userNickname) {
        return userDao.findByNickname(userNickname);
    }

    @Override
    public List<UserInfo> getUserList(Integer pageNo, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Page<UserInfo> userPage = userDao.findAll(pageRequest);
        return userPage.getContent();
    }

    @Override
    public UserInfo saveUser(UserInfo userInfo) {

        return userDao.save(userInfo);
    }

    @Override
    public UserInfo updateUser(UserInfo userInfo) {
        UserInfo user = userDao.findByNickname(userInfo.getNickname());
        UserInfo newUser = userDao.save(new UserInfo(
                user.getId(),
                userInfo.getName(),
                user.getPassword(),
                userInfo.getEmail(),
                userInfo.getNickname(),
                userInfo.getRole()
        ));
        return newUser;
    }

    @Override
    public Boolean deleteUser(String userNickname) {
        userDao.deleteByNickname(userNickname);
        return null;
    }
}
