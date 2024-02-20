package com.github.backend.service;


import com.github.backend.repository.*;
import com.github.backend.service.exception.CommonException;
import com.github.backend.service.mapper.ChatMapper;
import com.github.backend.web.dto.chatDto.ChatMessageResponseDto;
import com.github.backend.web.dto.chatDto.ChatRoomResponseDto;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.entity.*;
import com.github.backend.web.entity.custom.CustomMateDetails;
import com.github.backend.web.entity.custom.CustomUserDetails;
import com.github.backend.web.entity.enums.CareStatus;
import com.github.backend.web.entity.enums.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ServiceApplyRepository serviceApplyRepository;
    private final ChatRepository chatRepository;
    private final AuthRepository authRepository;
    private final MateRepository mateRepository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd. a h:mm");


    @Transactional
    public Long createRoom(CareEntity care){
        log.info("채팅방 생성 요청 들어왔습니다.");
        ChatRoomEntity newChatRoom = chatRoomRepository.save(ChatRoomEntity.builder().careCid(care.getCareCid()).build());
        return newChatRoom.getChatRoomCid();
    }

    @Transactional
    public ResponseEntity enterChatRoom(Long roomCid) {
        log.info("채팅방 입장 요청 들어왔습니다");
        ChatRoomEntity chatRoom = chatRoomRepository.findByChatRoomCid(roomCid);
        CareEntity care = serviceApplyRepository.findById(chatRoom.getCareCid()).orElseThrow(() -> new CommonException("해당 서비스가 존재하지 않습니다.", ErrorCode.FAIL_RESPONSE));

        if(care.getCareStatus() == CareStatus.WAITING){
            return ResponseEntity.badRequest().body(CommonResponseDto.builder().code(400).success(false).message("대기 중인 서비스입니다.").build());
        }

        if(chatRoom.getMateCid()==null){
            UserEntity user = care.getUser();
            MateEntity mate = care.getMate();
            log.info("채팅 이력이 존재하지 않아, 채팅 참가자를 설정했습니다." +
                    " 참가 UserId:"+user.getUserId()+", 참가 MateId:"+mate.getMateId());
            chatRoom.setUserCid(user.getUserCid());
            chatRoom.setMateCid(mate.getMateCid());
            chatRoomRepository.save(chatRoom);
            CommonResponseDto returnDto =  CommonResponseDto.builder().code(200).success(true)
                    .message("채팅방 참가자 설정이 완료되었습니다. 대화를 시작해주세요").build();
            return ResponseEntity.ok().body(returnDto);
        }
        else{
            log.info("해당 채팅방의 채팅 이력을 보여줍니다.");
            List<ChatEntity> chatList = chatRepository.findChatEntitiesByChatRoom(chatRoom);
            List<ChatMessageResponseDto> chatHistory = chatList.stream().map(ChatMapper.INSTANCE::chatEntityToDTO).toList();
            return ResponseEntity.ok().body(chatHistory);
        }
    }

    public List<ChatRoomResponseDto> findMateChatRoomList(CustomMateDetails customMateDetails) {
        log.info("해당 메이트가 참가하고 있는 대화방 목록 조회 요청이 들어왔습니다.");
        MateEntity mate = customMateDetails.getMate();
        List<ChatRoomEntity> chatRooms = chatRoomRepository.findAllByMateCid(mate.getMateCid());
        List<ChatRoomResponseDto> chatRoomResponseDtos = new ArrayList<>();

        for (ChatRoomEntity chatRoom : chatRooms) {
            UserEntity user = authRepository.findById(chatRoom.getUserCid()).orElseThrow();
            String formattedTime = chatRoom.getUpdatedAt().format(formatter);
            ChatRoomResponseDto chatRoomResponseDto = ChatRoomResponseDto.builder()
                    .chatRoomCid(chatRoom.getChatRoomCid())
                    .name(user.getName())
                    .time(formattedTime)
                    .build();
            chatRoomResponseDtos.add(chatRoomResponseDto);
        }

        chatRoomResponseDtos.sort(Comparator.comparing(ChatRoomResponseDto::getTime).reversed());
        return chatRoomResponseDtos;
    }

    public List<ChatRoomResponseDto> findUserChatRoomList(CustomUserDetails customUserDetails) {
        log.info("해당 사용자가 참가하고 있는 대화방 목록 조회 요청이 들어왔습니다.");
        UserEntity user = customUserDetails.getUser();
        List<ChatRoomEntity> chatRooms = chatRoomRepository.findAllByUserCid(user.getUserCid());
        List<ChatRoomResponseDto> chatRoomResponseDtos = new ArrayList<>();

        for (ChatRoomEntity chatRoom : chatRooms) {
            MateEntity mate = mateRepository.findById(chatRoom.getMateCid()).orElseThrow();
            String formattedTime = chatRoom.getUpdatedAt().format(formatter);
            ChatRoomResponseDto chatRoomResponseDto = ChatRoomResponseDto.builder()
                    .chatRoomCid(chatRoom.getChatRoomCid())
                    .name(mate.getName())
                    .time(formattedTime)
                    .build();
            chatRoomResponseDtos.add(chatRoomResponseDto);
        }
        chatRoomResponseDtos.sort(Comparator.comparing(ChatRoomResponseDto::getTime).reversed());
        return chatRoomResponseDtos;
    }
}