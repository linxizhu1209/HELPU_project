package com.github.backend.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.github.backend.repository.MateRepository;
import com.github.backend.repository.ProfileImageRepository;
import com.github.backend.repository.ServiceApplyRepository;
import com.github.backend.service.mapper.MateCaringMapper;
import com.github.backend.web.dto.AppliedCaringDto;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.dto.request.RequestMateDto;
import com.github.backend.web.entity.CareEntity;
import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.ProfileImageEntity;
import com.github.backend.web.entity.custom.CustomUserDetails;
import com.github.backend.web.entity.enums.CareStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
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

    public CommonResponseDto updateInfo(RequestMateDto requestMateDto, MultipartFile profileImages) {

        if(requestMateDto.getPassword() != null)
          requestMateDto.setPassword(passwordEncoder.encode(requestMateDto.getPassword()));

        return CommonResponseDto.builder().code(200).success(true).message("이미지 업로드가 완료되었습니다!").build();
    }
}
