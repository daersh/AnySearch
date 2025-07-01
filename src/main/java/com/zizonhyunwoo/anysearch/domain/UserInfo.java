package com.zizonhyunwoo.anysearch.domain;

import com.zizonhyunwoo.anysearch.util.PasswordEncoder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_info")
public class UserInfo {

    @Id
    @Column(name = "id",unique = true,nullable = false)
    UUID id;
    @Column(name = "name",nullable = false)
    String name;
    @Column(name = "password", nullable = false)
    String password;
    @Column(name = "email", nullable = false, unique = true)
    String email;
    @Column(name = "nickname", nullable = false, unique = true)
    String nickname;
    @Column(name = "role", nullable = false)
    String role;

    public UserInfo(String name, String password, String email, String nickname) {
        this.name = name;
        this.password =  PasswordEncoder.encode(password);
        this.email = email;
        this.nickname = nickname;
    }

    public UserInfo(String userName, String password, String userEmail, String userNickname, String userRole) {
        this.name = userName;
        this.password =  PasswordEncoder.encode(password);
        this.email = userEmail;
        this.nickname = userNickname;
        this.role = userRole;
    }
}
