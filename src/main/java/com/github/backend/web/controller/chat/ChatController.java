package com.github.backend.web.controller.chat;




import com.github.backend.config.security.JwtTokenProvider;
import com.github.backend.service.ChatService;

import com.github.backend.web.dto.chatDto.ChatMessageRequestDto;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;

import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ChatController {
    private final ChatService chatService;
    private final JwtTokenProvider jwtTokenProvider;

    @MessageMapping(value = "/chat/message/{roomCid}")
    public void sendMessage(@Payload ChatMessageRequestDto message, @DestinationVariable("roomCid") Long roomCid, @Header("Authorization") String token) {
        String sender = jwtTokenProvider.getIdByToken(token);
        log.info(sender);
        chatService.sendMessage(message, roomCid, sender);
    }



}
