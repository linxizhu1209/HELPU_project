package com.github.backend.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@AllArgsConstructor
@Table(name = "chat_room_table")
public class ChatRoomEntity extends BaseEntity{
    @Id
    @Column(name = "chat_room_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "채팅 고유 아이디")
    private Long chatRoomCid;

    @Column(name = "chat_room_name")
    @Schema(description = "채팅방 이름")
    private String chatRoomName;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
    private List<ChatEntity> chatMessageList;


    @ManyToOne
    @JoinColumn(name = "user_cid")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "mate_cid")
    private MateEntity mate;
}
