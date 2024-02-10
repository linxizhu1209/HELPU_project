package com.github.backend.repository;

import com.github.backend.web.entity.CareEntity;
import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.UserEntity;
import com.github.backend.web.entity.enums.CareStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ServiceApplyRepository extends JpaRepository<CareEntity, Long > {

    List<CareEntity> findAllByMateAndCareStatus(MateEntity mate, CareStatus status);

    List<CareEntity> findAllByCareStatus(CareStatus careStatus);

    List<CareEntity> findAllByUserAndCareStatus(UserEntity userEntity, CareStatus careStatus);

    Integer countByCareStatus(CareStatus careStatus);

    @Query("SELECT c.mate FROM CareEntity c " +
          "INNER JOIN c.mate m " +
          "WHERE m.mateCid = :mateCid")
    Optional<CareEntity> findByMateCid(Long mateCid);
}
