package com.github.backend.web.dto;


import com.github.backend.web.entity.ChatRoomEntity;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomRequestDto {
    private Long userCid;
    private Long mateCid;
}