package com.github.backend.service.mapper;
import com.github.backend.web.dto.master.UserListDto;
import com.github.backend.web.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target="cid",source = "userCid")
    @Mapping(target="userName",source="name")
    @Mapping(target="userId",source = "userId")
    @Mapping(target="userGender",source="gender")
    @Mapping(target="isBlacklisted",source="blacklisted")
    UserListDto userEntityToDTO(UserEntity userEntity);
}
