package com.github.backend.web.controller.chat;




import com.github.backend.service.ChatService;

import com.github.backend.web.dto.chatDto.ChatMessageRequestDto;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;

import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatController {
    private final ChatService chatService;

    @MessageMapping(value = "/chat/message/{roomCid}")
    public void sendMessage(@Payload ChatMessageRequestDto message,@DestinationVariable("roomCid") Long roomCid) {
        chatService.sendMessage(message, roomCid);
    }



}
