package com.github.backend.web.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "roles_table")
public class RolesEntity {
    @Id
    @Column(name = "roles_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "권한 고유 아이디")
    private Long rolesCid;

    @Column(name = "roles_name")
    @Schema(description = "권한 이름")
    private Long rolesName;
}
