package com.github.backend.web.dto.chatDto;

import com.github.backend.web.entity.ChatEntity;
import com.github.backend.web.entity.ChatRoomEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageRequestDto {
    private String message;
    private String sender;


    @Builder
    public ChatMessageRequestDto(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }





    //Todo Entity to Dto
}