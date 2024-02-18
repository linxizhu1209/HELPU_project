package com.github.backend.web.dto.mates;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class CaringDto {
    @Schema(description = "동행 신청 고유 번호",example = "1")
    private Long careCid;
    @Schema(description = "도움서비스 내용",example = "서울 OO병원 동행 서비스")
    private String content;
    @Schema(description = "동행 날짜",example = "2024-2-10")
    private LocalDate date;
    @Schema(description = "동행 시작 시간",example = "09:00")
    private LocalTime startTime;
    @Schema(description = "동행 종료 시간",example = "11:00")
    private LocalTime finishTime;
    @Schema(description = "목적지",example = "00병원")
    private String arrivalLoc;
    @Schema(description = "유저의 프로필 사진 이름",example="피카츄.png")
    private String imagename;
    @Schema(description = "유저 프로필사진 주소",example = "https://qewr.com")
    private String imageAddress;
}
