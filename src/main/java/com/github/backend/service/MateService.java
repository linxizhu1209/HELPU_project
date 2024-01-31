package com.github.backend.service;

import com.github.backend.repository.CareRepository;
import com.github.backend.repository.MateRepository;
import com.github.backend.service.mapper.MateCaringMapper;
import com.github.backend.web.dto.AppliedCaringDto;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.entity.CareEntity;
import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.enums.CareStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MateService {

    private final CareRepository careRepository;
    private final MateRepository mateRepository;

    public CommonResponseDto applyCaring(Long careId) {
        CareEntity care = careRepository.findById(careId).orElseThrow();
        MateEntity mate = mateRepository.findById(mateId).orElseThrow();
        // 만약 동일한 날짜 같은 시간대에 이미 신청한 도움이 있다면 신청불가능하게끔하기
        care.setMate(mate);
        care.setCareStatus(CareStatus.IN_PROGRESS);
        careRepository.save(care);
        return CommonResponseDto.builder().code(200).success(true).message("도움 지원이 완료되었습니다!").build();
    }

    public List<AppliedCaringDto> viewApplyList(String careStatus) {
//        try {
            MateEntity mate = mateRepository.findById(mateId).orElseThrow();
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
}
