package com.github.backend.web.controller.chat;


import com.github.backend.service.ChatRoomService;
import com.github.backend.web.dto.chatDto.ChatRoomResponseDto;
import com.github.backend.web.entity.custom.CustomMateDetails;
import com.github.backend.web.entity.custom.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatrooms")
@Slf4j
@Tag(name = "채팅방 API", description = "채팅방 설정 및 조회")
public class ChatRoomController {
    public final ChatRoomService chatRoomService;

    /** ChatRoom 입장(채팅방에서 대화시작) */
    @Operation(summary = "채팅방 입장, 이력", description = "첫 채팅방 입장시 채팅방에 정보를 주입합니다. 처음이 아닐 경우 채팅 이력을 반환합니다.")
    @PutMapping("/{roomCid}")
    public ResponseEntity<?> chatRoom(
            @PathVariable Long roomCid){
        return chatRoomService.enterChatRoom(roomCid);
    }


    /** ChatRoom 조회 */
    @Operation(summary = "유저 채팅방 조회", description = "유저의 채팅방 목록을 조회합니다.")
    @GetMapping("/userChatList")
    public ResponseEntity<List<ChatRoomResponseDto>> getAllChatList(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<ChatRoomResponseDto> chatRoomList = chatRoomService.findUserChatRoomList(customUserDetails);
        return ResponseEntity.ok().body(chatRoomList);
    }

    @Operation(summary = "메이트 채팅방 조회", description = "메이트 채팅방 목록을 조회합니다.")
    @GetMapping("/mateChatList")
    public ResponseEntity<List<ChatRoomResponseDto>> getAllChatList(@AuthenticationPrincipal CustomMateDetails customMateDetails){
        List<ChatRoomResponseDto> chatRoomList = chatRoomService.findMateChatRoomList(customMateDetails);
        return ResponseEntity.ok().body(chatRoomList);
    }

}
