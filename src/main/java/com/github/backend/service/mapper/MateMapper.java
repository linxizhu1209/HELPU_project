package com.github.backend.service.mapper;

import com.github.backend.web.dto.ApplyMateDto;
import com.github.backend.web.entity.MateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MateMapper {
    MateMapper INSTANCE = Mappers.getMapper(MateMapper.class);

    @Mapping(target="mateNickname",source="nickname")
    @Mapping(target="mateGender",source="gender")
    @Mapping(target="mateAddress",source="address")
//    @Mapping(target="mateAge",source="")
//    @Mapping(target="certificateList",source="")
    ApplyMateDto MateEntityToDTO(MateEntity mateEntity);



}
