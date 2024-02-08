package com.github.backend.web.dto.mates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MyPageDto {

    private int waitingCount;
    private int inProgressCount;
    private int finishCount;
    private int cancelCount;
    private double mateRating;
}
