package com.github.backend.web.dto.mates;

import com.github.backend.web.entity.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class CaringDetailsDto {
private Long careCid;
private String userId;
private LocalDate date;
private LocalTime startTime;
private LocalTime finishTime;
private String arrivalLoc;
private Long cost;
private String content;
private Gender gender;
}
