package com.github.backend.web.dto;

import com.github.backend.web.entity.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDetailDto {
    private String userId;
    private String userName;
    private String phone;
    private String email;
    private Gender userGender;
}
