package com.github.backend.repository;


import com.github.backend.web.entity.ChatRoomEntity;
import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.UserEntity;
import org.springdoc.core.converters.models.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {

    List<ChatRoomEntity> findAllByUserCid(Long userCid);


    List<ChatRoomEntity> findAllByMateCid(Long mateCid);

    ChatRoomEntity findByChatRoomCid(Long roomCid);
    ChatRoomEntity findByCareCid(Long careCid);

}