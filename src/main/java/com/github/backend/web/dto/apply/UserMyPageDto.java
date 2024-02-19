package com.github.backend.web.dto.apply;


import io.swagger.v3.oas.annotations.media.Schema;
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
    private String imageName;
    private String imageAddress;
}
