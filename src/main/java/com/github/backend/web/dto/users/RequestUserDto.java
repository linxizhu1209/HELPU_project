package com.github.backend.web.dto.users;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 회원가입 및 수정 등에서 사용

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestUserDto {
    private String userId;
    private String password;
    private String email;
    private String nickname;
    private String phoneNumber;
    private String gender;
    private String address;
    private Long roles;
}
