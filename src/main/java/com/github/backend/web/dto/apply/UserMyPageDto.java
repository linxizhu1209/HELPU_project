package com.github.backend.web.dto.apply;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserMyPageDto {
    private String userId;
    private Long waitingCount;
    private Long proceedingCount;
    private Long completedCount;
    private Long cancelledCount;
}
