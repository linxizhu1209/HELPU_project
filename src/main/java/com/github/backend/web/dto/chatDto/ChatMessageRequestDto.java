package com.github.backend.web.dto.chatDto;

import com.github.backend.web.entity.ChatEntity;
import com.github.backend.web.entity.ChatRoomEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageRequestDto {
    private String message;
    private String sender;
    private Long sendAt;
    private String profileImage;
    @Builder
    public ChatMessageRequestDto(String message) {
        this.message = message;
    }





    //Todo Entity to Dto
}