package com.github.backend.web.dto;

import com.github.backend.web.entity.ChatRoomEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class ChatRoomResponseDto {
    private final String mateName;
}