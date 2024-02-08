package com.github.backend.web.dto.apply;
import com.github.backend.web.entity.enums.Gender;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class ServiceApplyDto {
    private String meetingDate;
    private String startTime;
    private String endTime;
    private String meetingLoc;
    private String destination;
    private Gender gender;
    private Long cost;
    private String content;
}
