package com.github.backend.web.dto.users;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 유저 회원가입 DTO

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestSaveUserDto {
    @Schema(description = "유저 아이디(닉네임)", example = "test1234")
    private String userId;
    @Schema(description = "유저 패스워드", example = "qwer1234")
    private String password;
    @Schema(description = "유저 이메일", example = "test@test.com")
    private String email;
    @Schema(description = "유저 이름(본명)", example = "헤헤헤")
    private String name;
    @Schema(description = "유저 휴대폰번호", example = "010-1111-3333")
    private String phoneNumber;
    @Schema(description = "유저 성별", example = "남자")
    private String gender;
    @Schema(description = "유저 주소", example = "서울시")
    private String address;
    @Schema(description = "유저 권한(기본값 : 3)", example = "3")
    private Long roles;
}
