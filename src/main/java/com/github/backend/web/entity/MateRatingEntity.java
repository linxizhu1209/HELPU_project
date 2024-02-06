package com.github.backend.web.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Setter
@Table(name = "mate_rating_table")
public class MateRatingEntity {
    @Id
    @Column(name = "rating_cid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "평점 고유 아이디")
    private Long ratingCid;

    @Column(name = "total_rating")
    @Schema(description = "총 평점")
    private Double totalRating;

    @Column(name = "rating_count")
    @Schema(description = "평가 횟수")
    private Integer ratingCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mate_cid", referencedColumnName = "mate_cid")
    private MateEntity mate;


    public static MateRatingEntity createNewMateRating(Double totalRating, Integer ratingCount, MateEntity mate) {
        MateRatingEntity mateRating = new MateRatingEntity();
        mateRating.setTotalRating(totalRating);
        mateRating.setRatingCount(ratingCount);
        mateRating.setMate(mate);
        return mateRating;
    }
}
