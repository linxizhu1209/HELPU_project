package com.github.backend.repository;

import com.github.backend.web.entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<RolesEntity, Long> {
    boolean existsByRolesName(String roleName);
}
