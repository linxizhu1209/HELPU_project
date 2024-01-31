package com.github.backend.service;

import com.github.backend.repository.MateRepository;
import com.github.backend.service.mapper.MateMapper;
import com.github.backend.web.dto.ApplyMateDto;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.RolesEntity;
import com.github.backend.web.entity.UserEntity;
import com.github.backend.web.entity.enums.MateStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MasterService {
    
    private final MateRepository mateRepository;
    private final SendMessageService sendMessageService;

    public CommonResponseDto registerMate(Long mateCid,boolean isAccept) {
        MateEntity mate = mateRepository.findById(mateCid).orElseThrow();
        String messageContent = "";
        if(isAccept) {
            mate.setMateStatus(MateStatus.COMPLETE);
            messageContent = mate.getNickname()+"님의 메이트 인증이 허가되었습니다! 로그인할 수 있습니다. ";
        }
        else{
            mate.setMateStatus(MateStatus.FAILED);
            messageContent = mate.getNickname()+"님의 메이트 인증이 실패하였습니다! 사유를 확인하시고 다시 인증해주세요. ";
        }
        String phoneNum = mate.getPhoneNumber();
        mateRepository.save(mate);
        sendMessageService.sendMessage(phoneNum,messageContent);
        return CommonResponseDto.builder().code(200).success(true).message("해당 회원의 메이트 인증요청을 처리했습니다.").build();
    }

    public List<ApplyMateDto> findApplyMateList() {
        List<MateEntity> mateList = mateRepository.findAllByMateStatus(MateStatus.PREPARING);
        return mateList.stream().map(MateMapper.INSTANCE::MateEntityToDTO).toList();
    }
}
