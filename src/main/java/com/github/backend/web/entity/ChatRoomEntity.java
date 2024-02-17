package com.github.backend.web.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name = "care_cid")
    @Schema(description = "채팅방과 연관된 도움cid")
    private Long careCid;

//    @JsonManagedReference
//    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
//    private List<ChatEntity> chatMessageList;

    @Column(name ="user_cid")
    private Long userCid;

    @Column(name="mate_cid")
    private Long mateCid;
}
