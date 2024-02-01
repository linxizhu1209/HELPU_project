package com.github.backend.web.dto.apply;
import com.github.backend.web.entity.enums.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Builder
public class ServiceApplyDto {
    private Long userCid;
    private String meetingDate;
    private String startTime;
    private String endTime;
    private String meetingLoc;
    private String destination;
    private Gender gender;
    private Long cost;

    @Override
    public String toString() {
        return "ServiceApplyDto{" +
                "meetingDate=" + meetingDate +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", meetingLoc='" + meetingLoc + '\'' +
                ", destination='" + destination + '\'' +
                ", Gender='" + gender + '\'' +
                ", cost='" + cost + '\'' +
                '}';
    }
}
