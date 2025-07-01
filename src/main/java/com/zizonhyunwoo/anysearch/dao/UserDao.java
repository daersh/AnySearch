package com.zizonhyunwoo.anysearch.dao;

import com.zizonhyunwoo.anysearch.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserDao extends JpaRepository<UserInfo, UUID> {

    UserInfo findByNickname(String userNickname);

    void deleteByNickname(String userNickname);
}
