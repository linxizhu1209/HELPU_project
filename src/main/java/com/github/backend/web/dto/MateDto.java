package com.github.backend.web.dto;

import com.github.backend.web.entity.enums.Gender;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MateDto {

    private String mateNickname;
    private Gender mateGender;
    private int mateAge;
//    private String mateAddress;
//    private List<CertificateDto> certificateList; // 자격증 리스트

}
