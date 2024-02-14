package com.github.backend.service;


import com.github.backend.repository.AuthRepository;
import com.github.backend.repository.ChatRoomRepository;
import com.github.backend.repository.MateRepository;
import com.github.backend.repository.ServiceApplyRepository;
import com.github.backend.web.dto.ChatRoomResponseDto;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.entity.CareEntity;
import com.github.backend.web.entity.ChatRoomEntity;
import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.UserEntity;
import com.github.backend.web.entity.custom.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final AuthRepository authRepository;
    private final ServiceApplyRepository serviceApplyRepository;
    private final MateRepository mateRepository;

    /** ChatRoom 생성 */
    @Transactional
    public CommonResponseDto save(Long careCid) {
       CareEntity careEntity = serviceApplyRepository.findById(careCid).orElseThrow();
       MateEntity mate = mateRepository.findById(careEntity.getMate().getMateCid()).orElseThrow();
       UserEntity user = authRepository.findById(careEntity.getUser().getUserCid()).orElseThrow();

            ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
                    .user(user)
                    .mate(mate)
                    .build();

            chatRoomRepository.save(chatRoomEntity);
        return CommonResponseDto.builder().code(200).success(true).message("채팅방이 생성되었습니다.").build();
    }


    public List<ChatRoomResponseDto> findAllChatRoomList() {
        List<ChatRoomEntity> chatRooms = chatRoomRepository.findAll();

        List<ChatRoomResponseDto> chatRoomResponseDtos = new ArrayList<>();

        for (ChatRoomEntity chatRoom : chatRooms) {
            ChatRoomResponseDto chatRoomResponseDto = ChatRoomResponseDto.builder()
                    .mateName(chatRoom.getMate().getName())
                    .build();

            chatRoomResponseDtos.add(chatRoomResponseDto);
        }
        return chatRoomResponseDtos;
    }
}