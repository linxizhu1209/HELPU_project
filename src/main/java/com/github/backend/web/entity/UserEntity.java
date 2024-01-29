package com.github.backend.web.entity;

import com.github.backend.web.entity.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users_table")
public class UserEntity extends BaseEntity{
    @Id
    @Column(name = "user_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "유저 고유 아이디")
    private Long userCid;

    @NotNull
    @Column(name = "user_id", length = 30)
    @Schema(description = "유저 아이디", example = "supercoding@admin.com")
    private String userId;

    @NotNull
    @Column(name = "password")
    @Schema(description = "유저 비밀번호", example = "qwer1234")
    private String password;

    @Column(name = "email", length = 30)
    @Schema(description = "유저 이름", example = "판매자")
    private String email;

    @Column(name = "phone_number", length = 30)
    @Schema(description = "휴대폰번호", example = "010-1111-2222")
    private String phoneNumber;

    @Column(name = "address", length = 100)
    @Schema(description = "주소", example = "어디일까요~")
    private String address;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "login_type", length = 100)
    @Schema(description = "로그인 형식", example = "GOOGLE")
    private String loginType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_cid", referencedColumnName = "profile_image_cid")
    private ProfileImageEntity profileImage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roles_cid", referencedColumnName = "roles_cid")
    private RolesEntity roles;

    @Column(name = "is_deleted", length = 10)
    @Schema(description = "삭제여부", example = "Y")
    private String isDeleted;

    @Column(name = "star_count")
    @Schema(description = "별점", example = "2.5")
    private Double starCount;

    @Builder
    public UserEntity(String userId, String password, String email, String phoneNumber, String address, String loginType, RolesEntity roles, String isDeleted, Double starCount){
      this.userId = userId;
      this.password = password;
      this.email = email;
      this.phoneNumber = phoneNumber;
      this.address = address;
      this.loginType = loginType;
      this.roles = roles;
      this.isDeleted = isDeleted;
      this.starCount = starCount;
    }
}
