package com.github.backend.web.dto.mates;

import com.github.backend.web.entity.enums.Gender;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MateDto {
    private String mateId;
    private String email;
    private Gender mateGender;
    private String mateName;
    private String registrationNum;
}
