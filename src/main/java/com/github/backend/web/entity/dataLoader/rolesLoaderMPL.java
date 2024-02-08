package com.github.backend.web.entity.dataLoader;

import com.github.backend.repository.RolesRepository;
import com.github.backend.web.entity.RolesEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
@RequiredArgsConstructor
@Slf4j
public class rolesLoaderMPL implements CommandLineRunner {

    private final RolesRepository rolesRepository;
    @Override
    public void run(String... args) throws Exception {
        createDefaultRoles();
    }

    private void createDefaultRoles() {
      String[] defaultRoles = {"ROLE_MASTER","ROLE_MATE","ROLE_USER"};

      // Check if the default user already exists

      for(String rolesName : defaultRoles){
          if (!rolesRepository.existsByRolesName(rolesName)) {
            RolesEntity defaultRole = RolesEntity.builder()
                    .rolesName(rolesName)
                    .build();

            rolesRepository.save(defaultRole);
            log.debug("[CreateRoles] 권한이 생성되었습니다. 추가된 권한 = " + rolesName);
        }else {
            log.debug("[CreateAdminUser] 이미 관리자 계정이 존재하여 생성하지 않습니다.");
        }
      }
    }
}
