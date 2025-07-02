package com.zizonhyunwoo.anysearch.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTest {

    class User {
        UUID id;
        String name;
        String password;
        String email;
        String nickname;

        public User(String name, String password, String email, String nickname) {
            id = UUID.randomUUID();
            this.name = name;
            this.password =  password;
            this.email = email;
            this.nickname = nickname;
        }
    }

    static class PasswordEncoder {
        static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        public static String encode(String password) {
           return bCryptPasswordEncoder.encode(password);
        }

        public static boolean match(String password, String password1) {
            return bCryptPasswordEncoder.matches(password, password1);
        }
    }

    @Test
    @DisplayName("사용자 정보")
    void 사용자_정보_등록(){

        String name="user Name 1";
        String password="user Password 1";
        String email="user Email 1";
        String nickname="user Nickname 1";
        User user= new User(name,password,email,nickname);

        assertEquals(name,user.name);
        assertEquals(email,user.email);
        assertEquals(nickname,user.nickname);
        assertTrue(PasswordEncoder.match(password,user.password));
        assertNotEquals(password, user.password);
    }

    @Test
    @DisplayName("사용자로그인")
    void 사용자_로그인_성공(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String name="user Name";
        String password="user Password";
        String email="user Email";
        String nickname="user Nickname";
        User user = new User(name,encoder.encode(password),email,nickname);

        String loginEmail="user Email";
        String loginPassword="user Password";

        assertEquals(loginEmail, user.email);
        assertTrue(encoder.matches(password,user.password));
        assertNotEquals(loginPassword, user.password);
    }

}
