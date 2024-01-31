package com.github.backend.service.mapper;

import com.github.backend.web.dto.AppliedCaringDto;
import com.github.backend.web.entity.CareEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MateCaringMapper {
        MateCaringMapper INSTANCE = Mappers.getMapper(MateCaringMapper.class);
        @Mapping(target="departureLoc",source="departureloc")
        @Mapping(target="arrivalLoc",source="arrivalloc")
        @Mapping(target="cost",source="cost")
        @Mapping(target="careDateTime",source="careDateTime")
        @Mapping(target="userNickname",source="user.nickname")
        AppliedCaringDto CareEntityToDTO(CareEntity careEntity);

    }

