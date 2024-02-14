package com.github.backend.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "chat_table")
public class ChatEntity extends BaseEntity{
    @Id
    @Column(name = "chat_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "채팅 고유 아이디")
    private Long chatCid;

    @Column(name = "content")
    @Schema(description = "채팅내용", example = "응?")
    private String content;

    @ManyToOne
    @JoinColumn(name="chat_room_cid")
    private ChatRoomEntity chatRoom;

    private String sender;

    @Builder
    public ChatEntity(String content, ChatRoomEntity chatRoom, String sender) {
        this.content = content;
        this.chatRoom = chatRoom;
        this.sender = sender;
    }
}
