package com.github.backend.web.controller.chat;


import com.github.backend.service.ChatRoomService;
import com.github.backend.web.dto.chatDto.ChatMessageResponseDto;
import com.github.backend.web.dto.chatDto.ChatRoomResponseDto;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.entity.custom.CustomMateDetails;
import com.github.backend.web.entity.custom.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatrooms")
public class ChatRoomController {
    public final ChatRoomService chatRoomService;

    /** ChatRoom 입장(채팅방에서 대화시작) */
    @PutMapping("/{chatRoomCid}")
    public ResponseEntity<?> chatRoom(
            @PathVariable Long chatRoomCid){
        // 만약 해당 대화방에 mate와 user가 없는 상태라면 mate와 user를
        //careEntity에서 찾아서 넣어주는 작업
        // 있다면 이전에 했던 대화내용 적당히 뿌려주기
        return chatRoomService.enterChatRoom(chatRoomCid);
    }


    /** ChatRoom 조회 */
    @GetMapping("/userChatList")
    public ResponseEntity<List<ChatRoomResponseDto>> getAllChatList(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<ChatRoomResponseDto> chatRoomList = chatRoomService.findUserChatRoomList(customUserDetails);
        return ResponseEntity.ok().body(chatRoomList);
    }

    @GetMapping("/mateChatlist")
    public ResponseEntity<List<ChatRoomResponseDto>> getAllChatList(@AuthenticationPrincipal CustomMateDetails customMateDetails){
        List<ChatRoomResponseDto> chatRoomList = chatRoomService.findMateChatRoomList(customMateDetails);
        return ResponseEntity.ok().body(chatRoomList);
    }

}
