package com.github.backend.web.dto;

import com.github.backend.web.entity.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserListDto {
    private String userId;
    private String userName;
    private Gender userGender;
    private boolean isBlacklisted;
}
