package com.github.backend.repository;

import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.RolesEntity;
import com.github.backend.web.entity.UserEntity;

import com.github.backend.web.entity.enums.MateStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;
import java.util.List;
import java.util.Optional;

@Repository
public interface MateRepository extends JpaRepository<MateEntity,Long> {

    List<MateEntity> findAllByMateStatus(MateStatus mateStatus);

    Optional<MateEntity> findByMateId(String mateId);
    boolean existsByMateId(String mateId);
}
