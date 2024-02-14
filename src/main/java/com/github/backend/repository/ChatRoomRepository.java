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
    Optional<ChatRoomEntity> findByUserAndMate(UserEntity user, MateEntity mate);

    Optional<List<ChatRoomEntity>> findAllByUser(UserEntity user);
}