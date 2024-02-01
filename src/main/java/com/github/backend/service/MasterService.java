package com.github.backend.service;

import com.github.backend.repository.MateRepository;
import com.github.backend.service.mapper.MateMapper;
import com.github.backend.service.mapper.UserMapper;
import com.github.backend.web.dto.ApplyMateDto;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.dto.RegisteredUser;
import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.RolesEntity;
import com.github.backend.web.entity.UserEntity;
import com.github.backend.web.entity.enums.MateStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MasterService {
    
    private final MateRepository mateRepository;
    private final SendMessageService sendMessageService;


    // 메이트 부분
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
        log.info("[GET:MASTER] 관리자의 메이트 조회 요청이 들어왔습니다.");
        List<MateEntity> mateList = mateRepository.findAllByMateStatus(MateStatus.PREPARING);
        return mateList.stream().map(MateMapper.INSTANCE::MateEntityToDTO).toList();
    }

    public ApplyMateDto findMate(Long mateCid) {
        log.info("[GET:MASTER] 관리자의 메이트 상세 조회 요청이 들어왔습니다.");
        MateEntity mate = mateRepository.findById(mateCid).orElseThrow();
        return ApplyMateDto.builder().mateAge(mate.getMateAge)
                .mateGender(mate.getGender()).mateNickname(mate.getNickname())
                .build();
    }

    public CommonResponseDto blackingMate(boolean isBlacklisted, Long mateCid) {
        log.info("[PUT:MASTER] 관리자의 사용자 블랙리스트 올리기/내리기 요청이 들어왔습니다.");
        if(isBlacklisted){
            MateEntity mate = mateRepository.findById(mateCid).orElseThrow();
            mate.setIsBlacked = true;
            mateRepository.save(mate);
        }
        else{
            MateEntity mate = mateRepository.findById(mateCid).orElseThrow();
            mate.setIsBlacked = false;
            mateRepository.save(mate);
        }
        return CommonResponseDto.builder().code(200).success(true).message("블랙리스트 요청이 성공적으로 처리됐습니다.").build();
    }





    // 사용자 부분
    public List<RegisteredUser> findAllUserList() {
        log.info("[GET:MASTER] 관리자의 사용자 조회 요청이 들어왔습니다.");
        List<UserEntity> userList = userRepository.findAll();
        return userList.stream().map(UserMapper.INSTANCE::userEntityToDTO).toList();
    }

    public CommonResponseDto blackingUser(boolean isBlacklisted,Long userCid) {
        log.info("[PUT:MASTER] 관리자의 사용자 블랙리스트 올리기/내리기 요청이 들어왔습니다.");
        if(isBlacklisted){
            UserEntity user = userRepository.findById(userCid);
            user.setIsBlacked = true;
            userRepository.save(user);
        }
        else{
            UserEntity user = userRepository.findById(userCid);
            user.setIsBlacked = false;
            userRepository.save(user);
        }
        return CommonResponseDto.builder().code(200).success(true).message("블랙리스트 요청이 성공적으로 처리됐습니다.").build();
    }


    public RegisteredUser findUser(Long userCid) {
        log.info("[GET:MASTER] 관리자의 사용자 상세 조회 요청이 들어왔습니다.");
        UserEntity user = userRepository.findById(userCid);
        return RegisteredUser.builder().userAge(user.getUserAge)
                .userGender(user.getGender())
                .username(user.getNickname()).build();

    }


}
