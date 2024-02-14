package com.github.backend.web.dto.chatDto;

import com.github.backend.web.entity.ChatRoomEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class ChatRoomResponseDto {
    @Schema(name="채팅 상대방 이름")
    private final String name;
}