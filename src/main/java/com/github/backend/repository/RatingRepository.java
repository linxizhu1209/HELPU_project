package com.github.backend.repository;


import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.MateRatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<MateRatingEntity, Long> {
    Optional<MateRatingEntity> findByMate(MateEntity mate);
}
