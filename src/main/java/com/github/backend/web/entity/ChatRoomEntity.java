package com.github.backend.web.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Column(name ="user_cid")
    private Long userCid;

    @Column(name="mate_cid")
    private Long mateCid;

    @JsonIgnore
    @Transient
    private Map<String,String> chatMessageMap = new HashMap<>();
    // 보낸 사람과, 메시지를 저장


    public void setChatMessageMap(ChatEntity chat) {
        this.chatMessageMap.put(chat.getSender(),chat.getMessage());
    }
}


