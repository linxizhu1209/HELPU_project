package com.github.backend.service.mapper;
import com.github.backend.web.dto.UserDto;
import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target="userName",source="name")
    @Mapping(target="userId",source = "userId")
    @Mapping(target="userGender",source="gender")
    UserDto userEntityToDTO(UserEntity userEntity);
}
