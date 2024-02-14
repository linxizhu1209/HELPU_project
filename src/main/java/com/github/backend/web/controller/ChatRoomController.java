package com.github.backend.web.controller;


import com.github.backend.service.ChatRoomService;
import com.github.backend.web.dto.ChatRoomRequestDto;
import com.github.backend.web.dto.ChatRoomResponseDto;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.entity.ChatRoomEntity;
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


    /** ChatRoom 생성 */
    @PostMapping
    public ResponseEntity<CommonResponseDto> createChatRoom(Long careCid) {
        CommonResponseDto newRoom = chatRoomService.save(careCid);
        return ResponseEntity.ok().body(newRoom);
    }

    /** ChatRoom 조회 */
    @GetMapping("/list")
    public ResponseEntity<List<ChatRoomResponseDto>> getAllChatList(){
        List<ChatRoomResponseDto> chatRoomList = chatRoomService.findAllChatRoomList();
        return ResponseEntity.ok().body(chatRoomList);
    }
}
