package com.github.backend.config.security.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

  private SecurityUtil() {}

  public static String getCurrentUserId() {
      final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      log.info("SecurityUtil: " + authentication);
      if (authentication == null || authentication.getName() == null) {
        throw new RuntimeException("인증 정보가 없습니다.");
      }

      log.info(authentication.getName());
      return authentication.getName();
  }
}
