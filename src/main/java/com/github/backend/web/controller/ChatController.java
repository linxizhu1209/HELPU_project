package com.github.backend.web.controller;




import com.github.backend.service.ChatService;

import com.github.backend.web.dto.ChatMessageRequestDto;
import com.github.backend.web.dto.ChatMessageResponseDto;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class ChatController {
    private final ChatService chatService;
    @MessageMapping("/chat/send")
    public void sendMessage(@Payload ChatMessageRequestDto message) {
        chatService.sendMessage(message);
    }


}
