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
@Table(name = "chat_table")
public class ChatEntity {
    @Id
    @Column(name = "chat_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "채팅 고유 아이디")
    private Long chatCid;

    @Column(name = "content")
    @Schema(description = "채팅내용", example = "응?")
    private String content;

    @Column(name = "created_at")
    @Schema(description = "채팅 보낸 시간")
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_cid", referencedColumnName = "user_cid")
    private UserEntity users;

}
