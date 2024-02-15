package com.github.backend.web.dto.master;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UnapprovedMateDto {
    @Schema(description = "미승인 사유",example = "미성년자인 관계로 메이트로 등록될 수 없습니다.")
    private String unapprovedReason;
}
