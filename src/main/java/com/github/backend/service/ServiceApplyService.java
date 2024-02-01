package com.github.backend.service;


import com.github.backend.ApplyMapper;
import com.github.backend.repository.ServiceApplyRepository;
import com.github.backend.web.dto.apply.CommonResponseDto;
import com.github.backend.web.dto.apply.ServiceApplyDto;
import com.github.backend.web.entity.CareEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceApplyService {
    private final ServiceApplyRepository serviceApplyRepository;

    public CommonResponseDto applyService(ServiceApplyDto serviceApplyDto) {
        Long userCid = serviceApplyDto.getUserCid();




        CareEntity careEntity = ApplyMapper.INSTANCE.careDtoToEntity(serviceApplyDto);
        log.info("careEntity: " + careEntity );
        serviceApplyRepository.save(careEntity);
        return CommonResponseDto.builder().code(200).success(true).message("서비스 신청이 완료되었습니다.").build();
    }
}
