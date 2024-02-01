package com.github.backend.service.mapper;

import com.github.backend.web.dto.ApplyMateDto;
import com.github.backend.web.dto.RegisteredUser;
import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.UserEntity;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target="username",source="nickname")
    @Mapping(target="userGender",source="gender")
    @Mapping(target="userAge",source="age") // age 엔티티 추가
    RegisteredUser userEntityToDTO(UserEntity userEntity);
}
