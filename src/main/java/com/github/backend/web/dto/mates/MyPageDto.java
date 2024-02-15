package com.github.backend.web.dto.mates;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MyPageDto {
    @Schema(description = "신규요청 건수",example = "3")
    private int waitingCount;
    @Schema(description = "진행중 건수",example = "5")
    private int inProgressCount;
    @Schema(description = "완료 건수",example = "1")
    private int finishCount;
    @Schema(description = "취소 건수",example = "2")
    private int cancelCount;
    @Schema(description = "메이트 별점",example = "4.5")
    private double mateRating;
}
