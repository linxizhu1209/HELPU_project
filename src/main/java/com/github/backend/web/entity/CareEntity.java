package com.github.backend.web.entity;

import com.github.backend.web.entity.enums.CareStatus;
import com.github.backend.web.entity.enums.Gender;
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
@Table(name = "care_table")
public class CareEntity extends BaseEntity{
    @Id
    @Column(name = "care_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "도움 고유 아이디")
    private Long careCid;

    @Column(name = "departure_loc")
    @Schema(description = "만남장소", example = "서울시 상세주소")
    private String departureLoc;

    @Column(name = "arrival_loc")
    @Schema(description = "목적지", example = "어디까지 감?")
    private String arrivalLoc;

    @Column(name = "cost")
    @Schema(description = "비용", example = "10000")
    private Long cost;

    @Column(name = "hope_gender")
    @Schema(description = "희망 성별", example = "남자")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "care_date_time")
    @Schema(description = "도움 시작시간")
    private LocalDateTime careDateTime;

    @Column(name = "required_time")
    @Schema(description = "도움 종료시간")
    private LocalDateTime requiredTime;

    @Column(name = "care_status")
    @Schema(description = "진행사항", example = "대기 중")
    @Enumerated(EnumType.STRING)
    private CareStatus careStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_cid", referencedColumnName = "user_cid")
    private UserEntity userCid;

}
