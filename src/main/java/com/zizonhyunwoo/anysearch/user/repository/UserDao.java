package com.zizonhyunwoo.anysearch.user.repository;

import com.zizonhyunwoo.anysearch.user.domain.UserInfo;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserDao extends JpaRepository<UserInfo, UUID> {

    UserInfo findByNickname(String userNickname);

    void deleteByNickname(String userNickname);

    UserInfo findByNicknameOrEmail(@NotEmpty String nicknameOrEmail, @NotEmpty String nicknameOrEmail1);
}
