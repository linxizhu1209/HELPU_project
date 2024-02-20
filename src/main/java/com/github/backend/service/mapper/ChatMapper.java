package com.github.backend.service.mapper;

import com.github.backend.web.dto.chatDto.ChatMessageResponseDto;
import com.github.backend.web.dto.mates.CaringDto;
import com.github.backend.web.entity.CareEntity;
import com.github.backend.web.entity.ChatEntity;
import com.github.backend.web.util.TimestampUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper
public interface ChatMapper {

    ChatMapper INSTANCE = Mappers.getMapper(ChatMapper.class);

    @Mapping(target="sender",source="sender")
    @Mapping(target="content",source="message")
    @Mapping(target="timeStamp",source="createdAt", qualifiedByName = "convertLocalDateTimeToTimestamp")
    ChatMessageResponseDto chatEntityToDTO(ChatEntity chatEntity);

    // 이 메서드는 TimestampUtil을 사용하여 LocalDateTime을 Timestamp로 변환.
    @Named("convertLocalDateTimeToTimestamp")
    default long convertLocalDateTimeToTimestamp(LocalDateTime localDateTime) {
      return TimestampUtil.convertLocalDateTimeToTimestamp(localDateTime);
    }
}
