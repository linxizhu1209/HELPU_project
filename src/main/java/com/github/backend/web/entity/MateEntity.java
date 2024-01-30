package com.github.backend.web.entity;

import com.github.backend.web.entity.enums.Gender;
import com.github.backend.web.entity.enums.MateStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "mates_table")
public class MateEntity extends BaseEntity{
  @Id
  @Column(name = "mate_cid")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "메이트 고유 아이디")
  private Long userCid;

  @NotNull
  @Column(name = "mate_id", length = 30)
  @Schema(description = "메이트 아이디", example = "supercoding@admin.com")
  private String userId;

  @NotNull
  @Column(name = "mate_password")
  @Schema(description = "메이트 비밀번호", example = "qwer1234")
  private String password;

  @Column(name = "mate_email", length = 30)
  @Schema(description = "메이트 이메일", example = "판매자")
  private String email;

  @Column(name = "mate_nickname", length = 30)
  @Schema(description = "메이트 닉네임", example = "판매자")
  private String nickname;

  @Column(name = "mate_phone", length = 30)
  @Schema(description = "메이트 휴대폰번호", example = "010-1111-2222")
  private String phoneNumber;

  @Column(name = "mate_address", length = 100)
  @Schema(description = "메이트 주소", example = "어디일까요~")
  private String address;

  @Column(name = "mate_gender")
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

  @Column(name = "register_status", length = 10)
  @Schema(description = "등록현황", example = "인증 전")
  @Enumerated(EnumType.STRING)
  private MateStatus mateStatus;
}
