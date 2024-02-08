package com.github.backend.web.entity.dataLoader;

import com.amazonaws.services.kms.model.NotFoundException;
import com.github.backend.repository.AuthRepository;
import com.github.backend.repository.RolesRepository;
import com.github.backend.web.entity.RolesEntity;
import com.github.backend.web.entity.UserEntity;
import com.github.backend.web.entity.enums.Gender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class adminLoaderMPL implements CommandLineRunner {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;

    @Override
    public void run(String... args) throws Exception {
      createDefaultUser();
    }

    @Value("${spring.default.admin_id}")
    private String defaultUserId;
    @Value("${spring.default.admin_pwd}")
    private String defaultPassword;
    private void createDefaultUser() {
      String defaultName = "관리자";
      String defaultEmail = "supercoding@admin.com";
      String defaultPhone = "010-1111-2222";
      String defaultAddress = "Admin Address";
      String defaultIsDeleted = "false";
      RolesEntity roles = rolesRepository.findById(1L).orElseThrow(() -> new NotFoundException("권한이 존재하지 않습니다."));

      // Check if the default user already exists
      if (!authRepository.existsByUserId(defaultUserId)) {
        UserEntity defaultUser = UserEntity.builder()
                .userId(defaultUserId)
                .password(passwordEncoder.encode(defaultPassword))
                .email(defaultEmail)
                .name(defaultName)
                .phoneNumber(defaultPhone)
                .address(defaultAddress)
                .isDeleted(defaultIsDeleted)
                .gender(Gender.WOMEN)
                .roles(roles)
                .build();
        log.debug("[CreateAdminUser] 관리자 계정이 생성되었습니다.");

        authRepository.save(defaultUser);
      } else {
        log.debug("[CreateAdminUser] 이미 관리자 계정이 존재하여 생성하지 않습니다.");
      }
    }
}
