package com.zizonhyunwoo.anysearch.user.service;

import com.zizonhyunwoo.anysearch.user.repository.UserDao;
import com.zizonhyunwoo.anysearch.user.domain.UserInfo;
import com.zizonhyunwoo.anysearch.common.util.JwtUtil;
import com.zizonhyunwoo.anysearch.common.util.PasswordEncoder;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final JwtUtil jwtUtil;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserInfo getUserByuserNickname(String userNickname) {
        return userDao.findByNickname(userNickname);
    }

    @Override
    public List<UserInfo> getUserList(Integer pageNo, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Slice<UserInfo> userPage = userDao.findAll(pageRequest);
        return userPage.getContent();
    }

    @Override
    @Transactional
    public UserInfo saveUser(UserInfo userInfo) {
        System.out.println("userInfo = " + userInfo);
        return userDao.save(
                new UserInfo(
                        userInfo.getName(),
                        passwordEncoder.encode(userInfo.getPassword()),
                        userInfo.getEmail(),
                        userInfo.getNickname(),
                        userInfo.getRole()));
    }

    @Override
    @Transactional
    public UserInfo updateUser(UserInfo userInfo) {
        UserInfo user = userDao.findByNickname(userInfo.getNickname());
        UserInfo newUser = userDao.save(new UserInfo(
                user.getId(),
                userInfo.getName(),
                passwordEncoder.encode(user.getPassword()),
                userInfo.getEmail(),
                userInfo.getNickname(),
                userInfo.getRole()
        ));
        return newUser;
    }

    @Override
    @Transactional
    public Boolean deleteUser(String userNickname) {
        userDao.deleteByNickname(userNickname);
        return null;
    }

    @Override
    public String login(LoginRequest req, HttpServletResponse response) {
        UserInfo userinfo = userDao.findByNicknameOrEmail(req.getNicknameOrEmail(), req.getNicknameOrEmail());
        System.out.println("userinfo = " + userinfo);
        System.out.println("req = " + req);
        if (passwordEncoder.matches(req.getPassword(), userinfo.getPassword())) {
            String token = jwtUtil.createJwt(userinfo,"access");
            log.info("new user login success: {}",userinfo);
            return token;
        }

        response.setStatus(400);
        return "login fail";
    }
}
