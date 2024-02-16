package com.github.backend.web.dto.mates;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 메이트 회원가입 DTO
@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestSaveMateDto {
    @Schema(description = "메이트 아이디(닉네임)", example = "mate1234")
    private String mateId;
    @Schema(description = "메이트 패스워드", example = "qwer1234")
    private String password;
    @Schema(description = "메이트 이메일", example = "mate@test.com")
    private String email;
    @Schema(description = "메이트 이름(본명)", example = "호호호")
    private String name;
    @Schema(description = "메이트 휴대폰번호", example = "010-1111-3333")
    private String phoneNumber;
    @Schema(description = "메이트 성별", example = "여자")
    private String gender;
    @Schema(description = "메이트 주소", example = "인천시")
    private String address;
    @Schema(description = "메이트 주민등록번호", example = "950000-1020200")
    private String registrationNum;
    @Schema(description = "메이트 권한(기본값 : 2)", example = "2")
    private Long roles;
}
