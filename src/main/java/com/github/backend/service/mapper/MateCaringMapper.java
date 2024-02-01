package com.github.backend.service.mapper;

import com.github.backend.web.dto.AppliedCaringDto;
import com.github.backend.web.entity.CareEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MateCaringMapper {
        MateCaringMapper INSTANCE = Mappers.getMapper(MateCaringMapper.class);
        @Mapping(target="careCid",source="careCid")
        @Mapping(target="userId",source="user.userId")
        @Mapping(target="arrivalLoc",source="arrivalLoc")
        @Mapping(target="cost",source="cost")
        @Mapping(target="date",source="careDate")
        @Mapping(target="startTime",source="careDateTime")
        @Mapping(target="finishTime",source="requiredTime")
//        @Mapping(target="content",source="content") // careEntity에 content관련 필드 생성 시 주석 해제
        @Mapping(target="gender",source="gender")
        AppliedCaringDto CareEntityToDTO(CareEntity careEntity);

    }

