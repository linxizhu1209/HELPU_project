package com.github.backend.service.mapper;

import com.github.backend.web.dto.mates.MateDto;
import com.github.backend.web.entity.MateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MateMapper {
    MateMapper INSTANCE = Mappers.getMapper(MateMapper.class);

    @Mapping(target="mateId",source="mateId")
    @Mapping(target="mateName",source="name")
    @Mapping(target="mateGender",source="gender")
    @Mapping(target="isBlacklisted",source="blacklisted")
    MateDto MateEntityToDTO(MateEntity mateEntity);

}
