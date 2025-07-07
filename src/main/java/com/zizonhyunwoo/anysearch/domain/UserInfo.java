package com.zizonhyunwoo.anysearch.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_info")
@ToString
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    UUID id;
    @Column(name = "name",nullable = false)
    String name;
    @Column(name = "password")
    String password;
    @Column(name = "email", unique = true)
    String email;
    @Column(name = "nickname", nullable = false, unique = true)
    String nickname;
    @Column(name = "role", nullable = false)
    String role;

    public UserInfo(String name, String password, String email, String nickname) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.role = "ROLE_USER";
    }

    public UserInfo(String userName, String password, String userEmail, String userNickname, String userRole) {
        this.name = userName;
        this.password = password;
        this.email = userEmail;
        this.nickname = userNickname;
        this.role = userRole;
    }
}
