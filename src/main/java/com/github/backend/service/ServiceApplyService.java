package com.github.backend.service;

import com.github.backend.repository.AuthRepository;
import com.github.backend.repository.ServiceApplyRepository;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.dto.apply.ServiceApplyDto;
import com.github.backend.web.entity.CareEntity;
import com.github.backend.web.entity.UserEntity;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@Slf4j
@Builder
@RequiredArgsConstructor
public class ServiceApplyService {
    private final ServiceApplyRepository serviceApplyRepository;
    private final AuthRepository authRepository;

    public CommonResponseDto applyService(ServiceApplyDto serviceApplyDto) {
        UserEntity userEntity = authRepository.findById(serviceApplyDto.getUserCid())
                .orElseThrow(() -> new IllegalArgumentException("해당 UserEntity를 찾을 수 없습니다."));

        CareEntity careEntity = CareEntity.builder()
                .user(userEntity)
                .departureLoc(serviceApplyDto.getMeetingLoc())
                .arrivalLoc(serviceApplyDto.getDestination())
                .careDate(LocalDate.parse(serviceApplyDto.getMeetingDate()))
                .careDateTime(LocalTime.parse(serviceApplyDto.getStartTime()))
                .requiredTime(LocalTime.parse(serviceApplyDto.getEndTime()))
                .gender(serviceApplyDto.getGender())
                .cost(serviceApplyDto.getCost())
                .build();

        serviceApplyRepository.save(careEntity);
        return CommonResponseDto.builder().code(200).success(true).message("서비스 신청이 완료되었습니다.").build();
    }
}
