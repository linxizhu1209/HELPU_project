package com.github.backend.web.dto;

import com.github.backend.web.entity.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDto {
    @Schema(description = "사용자 아이디",example = "linxizhu129")
    private String userId;
    @Schema(description = "사용자 본명",example = "김용용")
    private String userName;
    @Schema(description = "사용자 성별",example = "MEN/WOMEN")
    private Gender userGender;
}
