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
public class MainPageDto {
    @Schema(description = "신규요청 건수",example = "3")
    private int waitingCount;
}
