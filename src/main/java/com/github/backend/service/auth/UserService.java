package com.github.backend.service.auth;

import com.github.backend.config.security.util.SecurityUtil;
import com.github.backend.repository.AuthRepository;
import com.github.backend.repository.MateRepository;
import com.github.backend.service.exception.CommonException;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.dto.users.RequestUpdateDto;
import com.github.backend.web.dto.users.ResponseMyInfoDto;
import com.github.backend.web.dto.users.ResponseUserDto;
import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.UserEntity;
import com.github.backend.web.entity.custom.CustomMateDetails;
import com.github.backend.web.entity.custom.CustomUserDetails;
import com.github.backend.web.entity.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.github.backend.service.exception.NotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final AuthRepository authRepository;
    private final MateRepository mateRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
      UserEntity users = authRepository.findByUserId(userId).isPresent() == false ? null : authRepository.findByUserId(userId).get();
      MateEntity mates = mateRepository.findByMateId(userId).isPresent() == false ? null : mateRepository.findByMateId(userId).get();
      if(users == null){
        return new CustomMateDetails(mates);
      }if(mates == null){
        return new CustomUserDetails(users);
      }
      return null;
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

    // 현재 접속중인 사용자의 유저정보 페이지 로직
    public ResponseMyInfoDto findByUser(CustomUserDetails customUserDetails) {
        UserEntity user = authRepository.findById(customUserDetails.getUser().getUserCid()).orElseThrow(() -> new CommonException("userId: " + customUserDetails.getUser() + "를 데이터베이스에서 찾을 수 없습니다."));

        return ResponseMyInfoDto.builder()
                .cid(user.getUserCid())
                .name(user.getName())
                .id(user.getUserId())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    @Transactional
    public CommonResponseDto updateInfo(RequestUpdateDto requestUpdateDto, MultipartFile profileImages) {

      if(!authRepository.existsById(requestUpdateDto.getCid())){
          throw new CommonException("아이디가 존재하지 않습니다.", ErrorCode.FAIL_RESPONSE);
      }

      UserEntity user = authRepository.findById(requestUpdateDto.getCid()).get();
      if(requestUpdateDto.getPassword() != null) {
        requestUpdateDto.setPassword(passwordEncoder.encode(requestUpdateDto.getPassword()));
        user.setEmail(requestUpdateDto.getEmail());
        user.setPassword(requestUpdateDto.getPassword());
        user.setPhoneNumber(requestUpdateDto.getPhoneNumber());
      }else{
        user.setEmail(requestUpdateDto.getEmail());
        user.setPhoneNumber(requestUpdateDto.getPhoneNumber());
      }
      log.info("[build] update user = " + user);
      authRepository.save(user);

      //이미지 관련 처리가 되면 해당 내용 추가
//      if(profileImages != null) {
//          ProfileImageEntity uploadImages = imageUploadService.profileUploadImage(multipartFile);
//          user.setProfileImage(uploadImages);
//          authRepository.save(user);
//          log.info("[profileImage] 유저프로필 이미지가 추가되었습니다. uploadedImages = " + uploadImages);
//      }
      return CommonResponseDto.builder()
              .code(200)
              .message("유저 정보가 성공적으로 변경되었습니다.")
              .success(true)
              .build();


    }
}
