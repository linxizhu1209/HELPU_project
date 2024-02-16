package com.github.backend.service;


import com.github.backend.repository.ChatRepository;
import com.github.backend.repository.ChatRoomRepository;
import com.github.backend.web.dto.chatDto.ChatMessageRequestDto;

import com.github.backend.web.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void sendMessage(ChatMessageRequestDto message,Long roomId, String sender) {
        ChatRoomEntity chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));



        UserEntity senderUser = chatRoom.getUser();
        MateEntity senderMate = chatRoom.getMate();

        ChatEntity chatMessage;

        if (senderUser.getUserId().equals(sender)) {
            // 메시지를 보낸 사람이 User인 경우 처리
            chatMessage = ChatEntity.builder()
                    .content(message.getMessage())
                    .sender(sender)
                    .chatRoom(chatRoom)
                    .build();
        } else if (senderMate.getMateId().equals(sender)) {
            // 메시지를 보낸 사람이 Mate인 경우 처리
            chatMessage = ChatEntity.builder()
                    .content(message.getMessage())
                    .sender(sender)
                    .chatRoom(chatRoom)
                    .build();
        } else {
            throw new IllegalArgumentException("메시지를 보낸 사용자를 찾을 수 없습니다.");
        }

        if (chatMessage != null) {
            chatRepository.save(chatMessage); // chatRepository.save()를 한 번만 호출

            // 채팅 메시지를 구독 중인 클라이언트에게 전송
            messagingTemplate.convertAndSend("/queue/chat/message/" + chatRoom.getChatRoomCid(), chatMessage);
        }
    }

}
