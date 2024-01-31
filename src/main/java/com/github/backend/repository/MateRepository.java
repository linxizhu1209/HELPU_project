package com.github.backend.repository;

import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.enums.MateStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MateRepository extends JpaRepository<MateEntity,Long> {

    List<MateEntity> findAllByMateStatus(MateStatus mateStatus);
}
