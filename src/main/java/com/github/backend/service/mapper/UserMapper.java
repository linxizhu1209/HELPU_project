package com.github.backend.service.mapper;
import com.github.backend.web.dto.RegisteredUser;
import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.UserEntity;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target="username",source="userId")
    @Mapping(target="userGender",source="gender")
//    @Mapping(target="name",source="name") // userEntity에 본명추가시 주석 해제
    @Mapping(target="email",source="email")
    RegisteredUser userEntityToDTO(UserEntity userEntity);
}
