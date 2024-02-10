package com.github.backend.web.dto.mates;

import com.github.backend.web.entity.enums.Gender;
import com.github.backend.web.entity.enums.MateStatus;
import lombok.*;


@Getter
@Setter
@Builder
public class MateDetailDto {
    private String mateId;
    private String email;
    private String phoneNum;
    private Gender mateGender;
    private String mateName;
    private String registrationNum;
    private MateStatus mateStatus;
}
