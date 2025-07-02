package com.zizonhyunwoo.anysearch.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequest {

    @NotEmpty
    private String nicknameOrEmail;
    @NotEmpty
    private String password;


}
