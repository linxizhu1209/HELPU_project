package com.github.backend.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "chat_room_table")
public class ChatRoomEntity {
    @Id
    @Column(name = "chat_room_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "채팅 고유 아이디")
    private Long chatRoomCid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_cid", referencedColumnName = "user_cid")
    private UserEntity users;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mate_cid", referencedColumnName = "mate_cid")
    private MateEntity mates;
}
