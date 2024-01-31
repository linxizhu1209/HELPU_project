package com.github.backend;


import com.github.backend.web.dto.apply.ServiceApplyDto;
import com.github.backend.web.entity.CareEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ApplyMapper {
    ApplyMapper INSTANCE = Mappers.getMapper(ApplyMapper.class);

    @Mapping(source = "meetingDate", target = "careDate")
    @Mapping(source = "startTime", target = "careDateTime")
    @Mapping(source = "endTime", target = "requiredTime")
    @Mapping(source = "meetingLoc", target = "departureLoc")
    @Mapping(source = "destination", target = "arrivalLoc")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "cost", target = "cost")
    @Mapping(target = "careCid", ignore = true)
    @Mapping(target = "careStatus", ignore = true)
    @Mapping(source = "userCid", target = "userCid")
    @Mapping(target = "mate", ignore = true)

    CareEntity careDtoToEntity(ServiceApplyDto serviceApplyDto);

}
