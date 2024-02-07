package com.github.backend.web.dto.mates;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class CaringDto {
    private String content;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime finishTime;
    private String arrivalLoc;
}
