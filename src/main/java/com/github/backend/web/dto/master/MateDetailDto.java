package com.github.backend.web.dto.master;

import com.github.backend.web.entity.enums.Gender;
import com.github.backend.web.entity.enums.MateStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@Setter
@Builder
public class MateDetailDto {
    @Schema(description = "메이트 아이디",example = "linxizhu129")
    private String mateId;
    @Schema(description = "메이트 이메일",example = "abc@naver.com")
    private String email;
    @Schema(description = "핸드폰 번호",example = "01012345678")
    private String phoneNum;
    @Schema(description = "메이트 성별",example = "MEN/WOMEN")
    private Gender mateGender;
    @Schema(description = "메이트 본명",example = "김용용")
    private String mateName;
    @Schema(description = "메이트 주민번호",example = "971111-111111")
    private String registrationNum;
    @Schema(description = "메이트 인증상태",example = "PREPARING")
    private MateStatus mateStatus;
}
