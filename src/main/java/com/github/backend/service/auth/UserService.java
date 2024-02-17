package com.github.backend.service.auth;

import com.github.backend.repository.AuthRepository;
import com.github.backend.repository.MateRepository;
import com.github.backend.repository.ProfileImageRepository;
import com.github.backend.service.ImageUploadService;
import com.github.backend.service.exception.CommonException;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.dto.users.RequestUpdateDto;
import com.github.backend.web.dto.users.ResponseMyInfoDto;
import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.ProfileImageEntity;
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

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final AuthRepository authRepository;
    private final MateRepository mateRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageUploadService imageUploadService;
    private final ProfileImageRepository profileImageRepository;

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

      if(requestUpdateDto.getPhoneNumber() == null){
          throw new CommonException("휴대폰 번호는 입력되어있어야 합니다.", ErrorCode.BAD_REQUEST_RESPONSE);
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

      if(profileImages != null) {
          // 수정 전 이미 이미지 파일을 가지고 있는 경우
          if(user.getProfileImage() != null){
            ProfileImageEntity preProfileImage = profileImageRepository.findById(
                    user.getProfileImage().getProfileImageCid())
                    .orElseThrow(() -> new CommonException("프로필 이미지를 찾을 수 없습니다.", ErrorCode.FAIL_RESPONSE));
            // S3에 이미지 파일 삭제 처리 진행
            imageUploadService.deleteImage(preProfileImage.getFileUrl());
            // 이미지 파일 테이블에 정보도 삭제
            profileImageRepository.deleteById(preProfileImage.getProfileImageCid());
          }
          //이미지 파일 추가 로직 진행
          ProfileImageEntity uploadImages = imageUploadService.profileUploadImage(profileImages);
          user.setProfileImage(uploadImages);
          authRepository.save(user);
          log.info("[profileImage] 유저프로필 이미지가 추가되었습니다. uploadedImages = " + uploadImages);
      }
      return CommonResponseDto.builder()
              .code(200)
              .message("유저 정보가 성공적으로 변경되었습니다.")
              .success(true)
              .build();
    }
}
