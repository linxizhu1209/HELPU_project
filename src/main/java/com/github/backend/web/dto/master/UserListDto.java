package com.github.backend.web.dto.master;

import com.github.backend.web.entity.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserListDto {

    @Schema(description = "사용자 cid", example = "2")
    private Long cid;
    @Schema(description = "사용자 아이디",example = "linxizhu129")
    private String userId;
    @Schema(description = "사용자 본명",example = "김용용")
    private String userName;
    @Schema(description = "사용자 성별",example = "MEN/WOMEN")
    private Gender userGender;
    @Schema(description = "블랙리스트 여부",example = "true")
    private boolean isBlacklisted;
    @Schema(description = "사용자 프로필 사진 이름",example = "피카츄.png")
    private String imageName;
    @Schema(description = "사용자 프로필 사진 주소",example = "http://ddd.com")
    private String imageAddress;
}
