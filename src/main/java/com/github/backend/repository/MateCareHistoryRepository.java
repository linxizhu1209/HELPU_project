package com.github.backend.repository;

import com.github.backend.web.entity.CareEntity;
import com.github.backend.web.entity.MateCareHistoryEntity;
import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.enums.MateCareStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MateCareHistoryRepository extends JpaRepository<MateCareHistoryEntity,Long> {
    MateCareHistoryEntity findByCareAndMateAndMateCareStatus(CareEntity care, MateEntity mate, MateCareStatus mateCareStatus);

    int countByMateCareStatusAndMate(MateCareStatus mateCareStatus,MateEntity mate);

    List<MateCareHistoryEntity> findAllByMateAndMateCareStatus(MateEntity mate, MateCareStatus mateCareStatus);
}
