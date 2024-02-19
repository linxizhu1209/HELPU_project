package com.github.backend.web.dto.users;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//유저, 메이트 회원정보 수정 DTO
@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestUpdateDto {
    @Schema(description = "cid", example = "2")
    private Long cid;
    @Schema(description = "email", example = "test1234@user.com")
    private String email;
    private String password;
    @Schema(description = "phoneNumber", example = "010-2020-0000")
    private String phoneNumber;
}
