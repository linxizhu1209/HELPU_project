package com.github.backend.web.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(ChatEntity.ChatEntityListener.class)
@Table(name = "chat_table")
public class ChatEntity extends BaseEntity{

    public static class ChatEntityListener {
        @PrePersist
        public void beforeSave(ChatEntity chat) {
            if (chat.getChatRoom() != null) {
                chat.getChatRoom().setUpdatedAt(LocalDateTime.now());
            }
        }
    }

    @Id
    @Column(name = "chat_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "채팅 고유 아이디")
    private Long chatCid;

    @Column(name = "content")
    @Schema(description = "채팅내용", example = "응?")
    private String message;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="chat_room_cid")
    private ChatRoomEntity chatRoom;

    @Column(name="sender")
    private String sender;

    @Builder
    public ChatEntity(String content, ChatRoomEntity chatRoom, String sender) {
        this.message = content;
        this.chatRoom = chatRoom;
        this.sender = sender;
    }
}
