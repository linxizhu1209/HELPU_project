package com.github.backend.service;

import com.github.backend.repository.MateRepository;
import com.github.backend.repository.ProfileImageRepository;
import com.github.backend.repository.ServiceApplyRepository;
import com.github.backend.service.exception.CommonException;
import com.github.backend.service.exception.NotFoundException;
import com.github.backend.service.mapper.MateCaringMapper;
import com.github.backend.web.dto.AppliedCaringDto;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.dto.mates.RequestSaveMateDto;
import com.github.backend.web.dto.users.RequestUpdateDto;
import com.github.backend.web.dto.users.ResponseMyInfoDto;
import com.github.backend.web.entity.CareEntity;
import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.UserEntity;
import com.github.backend.web.entity.custom.CustomMateDetails;
import com.github.backend.web.entity.custom.CustomUserDetails;
import com.github.backend.web.entity.enums.CareStatus;
import com.github.backend.web.entity.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MateService {

    private final ImageUploadService imageUploadService;
    private final ProfileImageRepository profileImageRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServiceApplyRepository careRepository;
    private final MateRepository mateRepository;

    public CommonResponseDto applyCaring(Long careId, CustomUserDetails customUserDetails) {
        Long mateId = customUserDetails.getUser().getUserCid();
        CareEntity care = careRepository.findById(careId).orElseThrow();
        MateEntity mate = mateRepository.findById(mateId).orElseThrow();
        // 만약 동일한 날짜 같은 시간대에 이미 신청한 도움이 있다면 신청불가능하게끔하기
        care.setMate(mate);
        care.setCareStatus(CareStatus.IN_PROGRESS);
        careRepository.save(care);
        return CommonResponseDto.builder().code(200).success(true).message("도움 지원이 완료되었습니다!").build();
    }

    public List<AppliedCaringDto> viewApplyList(String careStatus,CustomUserDetails customUserDetails) {
//        try {
            MateEntity mate = mateRepository.findById(customUserDetails.getUser().getUserCid()).orElseThrow();
            CareStatus status = CareStatus.valueOf(careStatus);
            List<CareEntity> careList = careRepository.findAllByMateAndCareStatus(mate, status);
            return careList.stream().map(MateCaringMapper.INSTANCE::CareEntityToDTO).toList();
//        } catch (IllegalArgumentException iae) {
//            // 존재하지 않는 상태입니다.
//            return throw new RuntimeException();
//        }
    }

    public CommonResponseDto finishCaring(Long careCid) {
        CareEntity care = careRepository.findById(careCid).orElseThrow();
        care.setCareStatus(CareStatus.HELP_DONE);
        careRepository.save(care);
        return CommonResponseDto.builder().code(200).success(true).message("도움이 완료되었습니다!").build();
    }

    public List<AppliedCaringDto> viewAllApplyList() {
        List<CareEntity> careList = careRepository.findAllByCareStatus(CareStatus.WAITING);
        return careList.stream().map(MateCaringMapper.INSTANCE::CareEntityToDTO).toList();
    }

    @Transactional
    public CommonResponseDto updateInfo(RequestUpdateDto requestUpdateDto, MultipartFile profileImages) {

      if(!mateRepository.existsById(requestUpdateDto.getCid())){
        throw new CommonException("아이디가 존재하지 않습니다.", ErrorCode.FAIL_RESPONSE);
      }

      MateEntity mate = mateRepository.findById(requestUpdateDto.getCid()).get();
      if(requestUpdateDto.getPassword() != null) {
        requestUpdateDto.setPassword(passwordEncoder.encode(requestUpdateDto.getPassword()));
        mate.setEmail(requestUpdateDto.getEmail());
        mate.setPassword(requestUpdateDto.getPassword());
        mate.setPhoneNumber(requestUpdateDto.getPhoneNumber());
      }else{
        mate.setEmail(requestUpdateDto.getEmail());
        mate.setPhoneNumber(requestUpdateDto.getPhoneNumber());
      }
      log.info("[build] update mate = " + mate);
      mateRepository.save(mate);

//      if(profileImages != null) {
//          ProfileImageEntity uploadImages = imageUploadService.profileUploadImage(multipartFile);
//          user.setProfileImage(uploadImages);
//          authRepository.save(user);
//          log.info("[profileImage] 메이트 프로필 이미지가 추가되었습니다. uploadedImages = " + uploadImages);
//      }
      return CommonResponseDto.builder()
              .code(200)
              .message("메이트 정보가 성공적으로 변경되었습니다.")
              .success(true)
              .build();
    }

  public ResponseMyInfoDto findByMate(CustomMateDetails customUserDetails) {
      MateEntity mate = mateRepository.findById(customUserDetails.getMate().getMateCid()).orElseThrow(() -> new NotFoundException("존재하지 않는 메이트입니다."));
      return ResponseMyInfoDto.builder()
            .cid(mate.getMateCid())
            .name(mate.getName())
            .id(mate.getMateId())
            .email(mate.getEmail())
            .phoneNumber(mate.getPhoneNumber())
            .build();
  }
}
