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
    @Mapping(target="email",source="email")
    @Mapping(target="mateName",source="name") // mateEntity에 본명 생기면 주석해제
    @Mapping(target="mateGender",source="gender")
    @Mapping(target="registrationNum",source="registrationNum") // mateEntity에 주민등록번호 생기면 주석해제
    MateDto MateEntityToDTO(MateEntity mateEntity);

}
