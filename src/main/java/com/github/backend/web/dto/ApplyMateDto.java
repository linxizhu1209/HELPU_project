package com.github.backend.web.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyMateDto {

    private String mateNickname;
    private String mateGender;
    private int mateAge;
    private String mateAddress;
    private List<CertificateDto> certificateList; // 자격증 리스트

}
