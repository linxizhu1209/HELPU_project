package com.github.backend.web.dto.apply;


import com.github.backend.web.entity.CareEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class UserDto {
    private String content;
    private String Date;
    private String Location;
    private Long careCid;

    public static List<UserDto> careEntityToUserDto(List<CareEntity> careList){
        List<UserDto> userDtos = new ArrayList<>();
        for (CareEntity careEntity : careList) {
            UserDto userDto = UserDto.builder()
                    .careCid(careEntity.getCareCid())
                    .Location(careEntity.getDepartureLoc())
                    .content(careEntity.getContent())
                    .Date(convertDateToString(careEntity.getCareDate(), careEntity.getCareDateTime(), careEntity.getRequiredTime()))
                    .build();
            userDtos.add(userDto);
        }

        userDtos.sort(Comparator.comparing(UserDto::getCareCid).reversed());

        return userDtos;
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
