package com.github.backend.service;


import com.github.backend.repository.*;
import com.github.backend.web.dto.chatDto.ChatRoomResponseDto;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.entity.*;
import com.github.backend.web.entity.custom.CustomMateDetails;
import com.github.backend.web.entity.custom.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ServiceApplyRepository serviceApplyRepository;
    private final ChatRepository chatRepository;

    @Transactional
    public Long createRoom(CareEntity care){
        log.info("채팅방 생성 요청 들어왔습니다.");
        ChatRoomEntity chatRoom = ChatRoomEntity.builder()
                .chatRoomName(care.getContent()).build();
        ChatRoomEntity newChatRoom = chatRoomRepository.save(chatRoom);
        return newChatRoom.getChatRoomCid();
    }

    @Transactional
    public ResponseEntity enterChatRoom(Long chatRoomCid) {
        log.info("채팅방 입장 요청 들어왔습니다");
        ChatRoomEntity chatRoom = chatRoomRepository.findById(chatRoomCid).orElseThrow();
        if(chatRoom.getMate()==null){
            String chatRoomName = chatRoom.getChatRoomName();
            CareEntity care = serviceApplyRepository.findCareEntityByContent(chatRoomName);
            UserEntity user = care.getUser();
            MateEntity mate = care.getMate();
            log.info("채팅 이력이 존재하지 않아, 채팅 참가자를 설정했습니다." +
                    " 참가 UserId:"+user.getUserId()+", 참가 MateId:"+mate.getMateId());
            chatRoom.setUser(user);
            chatRoom.setMate(mate);
            chatRoomRepository.save(chatRoom);
            CommonResponseDto returnDto =  CommonResponseDto.builder().code(200).success(true)
                    .message("채팅방 참가자 설정이 완료되었습니다. 대화를 시작해주세요").build();
            return ResponseEntity.ok().body(returnDto);
        }
        else{
            log.info("해당 채팅방의 채팅 이력을 보여줍니다.");
            List<ChatEntity> chatList = chatRepository.findChatEntitiesByChatRoom(chatRoom);
            return ResponseEntity.ok().body(chatList);
        }
    }

    public List<ChatRoomResponseDto> findMateChatRoomList(CustomMateDetails customMateDetails) {
        log.info("해당 메이트가 참가하고 있는 대화방 목록 조회 요청이 들어왔습니다.");
        MateEntity mate = customMateDetails.getMate();
        List<ChatRoomEntity> chatRooms = chatRoomRepository.findAllByMate(mate);
        List<ChatRoomResponseDto> chatRoomResponseDtos = new ArrayList<>();

        for (ChatRoomEntity chatRoom : chatRooms) {
            ChatRoomResponseDto chatRoomResponseDto = ChatRoomResponseDto.builder()
                    .name(chatRoom.getUser().getName())
                    .build();

            chatRoomResponseDtos.add(chatRoomResponseDto);
        }
        return chatRoomResponseDtos;

    }

    public List<ChatRoomResponseDto> findUserChatRoomList(CustomUserDetails customUserDetails) {
        log.info("해당 사용자가 참가하고 있는 대화방 목록 조회 요청이 들어왔습니다.");
        UserEntity user = customUserDetails.getUser();
        List<ChatRoomEntity> chatRooms = chatRoomRepository.findAllByUser(user);
        List<ChatRoomResponseDto> chatRoomResponseDtos = new ArrayList<>();

        for (ChatRoomEntity chatRoom : chatRooms) {
            ChatRoomResponseDto chatRoomResponseDto = ChatRoomResponseDto.builder()
                    .name(chatRoom.getMate().getName())
                    .build();

            chatRoomResponseDtos.add(chatRoomResponseDto);
        }
        return chatRoomResponseDtos;
    }
}