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
    private String mateId;
    private String email;
    private Gender mateGender;
//    private String matename; // 메이트 본명 엔티티에 필드 생기면 주석 해제
//    private String registrationNum; // 주민등록번호 엔티티에 생기면 주석 해제

}
