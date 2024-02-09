package com.github.backend.web.entity;


import com.github.backend.web.entity.enums.CareStatus;
import com.github.backend.web.entity.enums.MateCareStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "mate_care_history_table")
public class MateCareHistoryEntity {
    @Id
    @Column(name="mate_care_history_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "메이트 도움 내역 고유 아이디")
    private Long mateCareHistoryCid;

    @Column
    @Schema(description = "메이트의 도움 상태",example = "취소")
    private MateCareStatus mateCareStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mate_cid", referencedColumnName = "mate_cid")
    private MateEntity mate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_cid", referencedColumnName = "care_cid")
    private CareEntity care;


}
