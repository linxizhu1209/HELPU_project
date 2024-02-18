package com.github.backend.service.mapper;

import com.github.backend.web.dto.chatDto.ChatMessageResponseDto;
import com.github.backend.web.dto.mates.CaringDto;
import com.github.backend.web.entity.CareEntity;
import com.github.backend.web.entity.ChatEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChatMapper {

    ChatMapper INSTANCE = Mappers.getMapper(ChatMapper.class);

    @Mapping(target="sender",source="sender")
    @Mapping(target="content",source="message")
    @Mapping(target="timeStamp",source="createdAt")
    ChatMessageResponseDto chatEntityToDTO(ChatEntity chatEntity);

}
