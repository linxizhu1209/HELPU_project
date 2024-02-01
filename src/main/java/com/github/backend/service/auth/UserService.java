package com.github.backend.service.auth;

import com.amazonaws.services.kms.model.NotFoundException;
import com.github.backend.config.security.util.SecurityUtil;
import com.github.backend.repository.AuthRepository;
import com.github.backend.web.dto.users.ResponseUserDto;
import com.github.backend.web.entity.UserEntity;
import com.github.backend.web.entity.custom.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
  private final AuthRepository authRepository;

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    UserEntity user = authRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("userId: " + userId + "를 데이터베이스에서 찾을 수 없습니다."));
    return new CustomUserDetails(user);
  }

  @Transactional(readOnly = true)
  public ResponseUserDto getLoginUserInfo(){
    return authRepository.findByUserId(SecurityUtil.getCurrentUserId())
            .map(ResponseUserDto::responseUserDto)
            .orElseThrow(() -> new NotFoundException("로그인 유저 정보가 없습니다."));
  }

  @Transactional(readOnly = true)
  public ResponseUserDto getLoginUserInfo(String userId){
    return authRepository.findByUserId(userId)
            .map(ResponseUserDto::responseUserDto)
            .orElseThrow(() -> new NotFoundException("유저 정보가 없습니다."));
  }
}
