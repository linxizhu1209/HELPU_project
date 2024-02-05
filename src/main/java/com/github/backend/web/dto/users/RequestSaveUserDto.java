package com.github.backend.web.dto.users;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 유저 회원가입 DTO

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestSaveUserDto {
    private String userId;
    private String password;
    private String email;
    private String name;
    private String phoneNumber;
    private String gender;
    private String address;
    private Long roles;
}
