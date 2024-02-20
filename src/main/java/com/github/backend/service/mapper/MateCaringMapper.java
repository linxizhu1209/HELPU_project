package com.github.backend.service.mapper;

import com.github.backend.web.dto.mates.CaringDto;
import com.github.backend.web.entity.CareEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MateCaringMapper {
        MateCaringMapper INSTANCE = Mappers.getMapper(MateCaringMapper.class);

        @Mapping(target="careCid",source="careCid")
        @Mapping(target="arrivalLoc",source="arrivalLoc")
        @Mapping(target="date",source="careDate")
        @Mapping(target="startTime",source="careDateTime")
        @Mapping(target="finishTime",source="requiredTime")
        @Mapping(target="content",source="content")
        @Mapping(target="myId",source="mate.mateId")
        CaringDto CareEntityToDTO(CareEntity careEntity);

    }

