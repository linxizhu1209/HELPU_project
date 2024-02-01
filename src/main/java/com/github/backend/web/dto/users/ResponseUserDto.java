package com.github.backend.web.dto.users;

import com.github.backend.web.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseUserDto {
    private String userId;

    public static ResponseUserDto responseUserDto(UserEntity userEntity){
        return new ResponseUserDto(userEntity.getUserId());
    }
}
