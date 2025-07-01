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
    @Column(name = "id")
    UUID id;
    @Column(name = "name")
    String name;
    @Column(name = "password")
    String password;
    @Column(name = "email")
    String email;
    @Column(name = "nickname")
    String nickname;
    @Column(name = "role")
    String role;

    public UserInfo(String name, String password, String email, String nickname) {
        this.name = name;
        this.password =  PasswordEncoder.encode(password);
        this.email = email;
        this.nickname = nickname;
    }

    public UserInfo(String userName, String s, String userEmail, String userNickname, String userRole) {
        this.name = name;
        this.password =  PasswordEncoder.encode(password);
        this.email = email;
        this.nickname = nickname;
        this.role = userRole;
    }
}
