package com.github.backend.service;


import com.github.backend.repository.AuthRepository;
import com.github.backend.repository.ChatRepository;
import com.github.backend.repository.ChatRoomRepository;
import com.github.backend.repository.MateRepository;
import com.github.backend.service.exception.CommonException;
import com.github.backend.web.dto.chatDto.ChatMessageRequestDto;

import com.github.backend.web.entity.*;
import com.github.backend.web.entity.enums.ErrorCode;
import com.github.backend.web.util.TimestampUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final AuthRepository authRepository;
    private final MateRepository mateRepository;
    //private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd. a h:mm");
    @Transactional
    public void sendMessage(ChatMessageRequestDto message,Long roomId) {
        ChatRoomEntity chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CommonException("채팅방을 찾을 수 없습니다.", ErrorCode.FAIL_RESPONSE));

        UserEntity senderUser = authRepository.findById(chatRoom.getUserCid()).orElseThrow();
        MateEntity senderMate = mateRepository.findById(chatRoom.getMateCid()).orElseThrow();

        ChatEntity chatMessage;

        if (senderUser.getUserId().equals(message.getSender())) {
            chatMessage = ChatEntity.builder()
                    .content(message.getMessage())
                    .sender(message.getSender())
                    .chatRoom(chatRoom)
                    .build();
            if(senderUser.getProfileImage() == null)
              message.setProfileImage(null);
            else
              message.setProfileImage(senderUser.getProfileImage().getFileUrl());
        } else if (senderMate.getMateId().equals(message.getSender())) {
            chatMessage = ChatEntity.builder()
                    .content(message.getMessage())
                    .sender(message.getSender())
                    .chatRoom(chatRoom)
                    .build();
            if(senderMate.getProfileImage() == null)
              message.setProfileImage(null);
            else
              message.setProfileImage(senderMate.getProfileImage().getFileUrl());
        } else {
            throw new CommonException("메시지를 보낸 사용자를 찾을 수 없습니다.", ErrorCode.FAIL_RESPONSE);
        }

        if (chatMessage != null) {
            chatRepository.save(chatMessage);
            Long formattedTime = TimestampUtil.convertLocalDateTimeToTimestamp(chatMessage.getCreatedAt());
            message.setSendAt(formattedTime);
            messagingTemplate.convertAndSend("/queue/chat/message/" + chatRoom.getChatRoomCid(), message);
        }
    }

}
