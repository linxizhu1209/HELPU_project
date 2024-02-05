package com.github.backend.web.dto.mates;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 메이트 회원가입 DTO
@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestSaveMateDto {
    private String mateId;
    private String password;
    private String email;
    private String name;
    private String phoneNumber;
    private String gender;
    private String address;
    private String registrationNum;
    private Long roles;
}
