package com.github.backend.repository;


import com.github.backend.web.entity.ChatEntity;
import com.github.backend.web.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {


    List<ChatEntity> findChatEntitiesByChatRoom(ChatRoomEntity chatRoom);
}