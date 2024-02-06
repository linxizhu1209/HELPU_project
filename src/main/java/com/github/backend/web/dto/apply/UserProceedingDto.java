package com.github.backend.web.dto.apply;

import com.github.backend.web.entity.CareEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Getter
@Builder
@AllArgsConstructor
public class UserProceedingDto {
    private String mateName;
    private String content;
    private String Date;
    private String Location;

    public static List<UserProceedingDto> careEntityToUserDto2(List<CareEntity> careList){
        List<UserProceedingDto> proceedingService = new ArrayList<>();
        for (CareEntity careEntity : careList) {
            UserProceedingDto userProceedingDto = UserProceedingDto.builder()
                    .mateName(careEntity.getMate().getName())
                    .Location(careEntity.getDepartureLoc())
                    .content(careEntity.getArrivalLoc())
                    .Date(convertDateToString(careEntity.getCareDate(), careEntity.getCareDateTime(), careEntity.getRequiredTime()))
                    .build();
            proceedingService.add(userProceedingDto);
        }
        return proceedingService;
    }


    private static String convertDateToString(LocalDate date, LocalTime careDateTime, LocalTime requiredTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
        String dateString = date.format(formatter);
        String timeString = careDateTime.format(formatter2);
        String requiredTimeString = requiredTime.format(formatter2);

        return dateString + " / " + timeString + " ~ " + requiredTimeString;
    }
}


