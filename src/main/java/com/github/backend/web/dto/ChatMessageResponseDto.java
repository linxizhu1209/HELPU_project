package com.github.backend.web.dto;

import com.github.backend.web.entity.ChatEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ChatMessageResponseDto {
    private String sender;
    private String content;
    private LocalDateTime timeStamp;



    //Todo Entity to Dto
}