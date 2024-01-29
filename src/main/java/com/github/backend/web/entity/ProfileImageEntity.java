package com.github.backend.web.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "profile_image_table")
public class ProfileImageEntity extends BaseEntity {
    @Id
    @Column(name = "profile_image_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileImageCid;

    @Column(name = "UUID")
    private String UUID;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "file_ext")
    private String fileExt;

}
