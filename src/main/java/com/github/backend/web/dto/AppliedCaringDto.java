package com.github.backend.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AppliedCaringDto {
private String userNickname;
private String departureLoc;
private String arrivalLoc;
private Long cost;
private LocalDateTime careDateTime;


}
