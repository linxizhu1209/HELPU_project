package com.github.backend.service;

import com.github.backend.repository.AuthRepository;
import com.github.backend.repository.MateRepository;
import com.github.backend.repository.RolesRepository;
import com.github.backend.service.mapper.MateMapper;
import com.github.backend.service.mapper.UserMapper;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.dto.UnapprovedMateDto;
import com.github.backend.web.dto.UserDetailDto;
import com.github.backend.web.dto.UserDto;
import com.github.backend.web.dto.mates.MateDetailDto;
import com.github.backend.web.dto.mates.MateDto;
import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.RolesEntity;
import com.github.backend.web.entity.UserEntity;
import com.github.backend.web.entity.enums.MateStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MasterService {
    
    private final MateRepository mateRepository;
    private final SendMessageService sendMessageService;
    private final AuthRepository authRepository;
    private final RolesRepository rolesRepository;


    // 메이트 부분
    public CommonResponseDto approveMate(Long mateCid) {
        MateEntity mate = mateRepository.findById(mateCid).orElseThrow();
        mate.setMateStatus(MateStatus.COMPLETE);
        String messageContent = mate.getName()+"님의 메이트 인증이 허가되었습니다! 로그인할 수 있습니다. ";
        String phoneNum = mate.getPhoneNumber();
        mateRepository.save(mate);
//        sendMessageService.sendMessage(phoneNum,messageContent);
        return CommonResponseDto.builder().code(200).success(true).message("해당 메이트 인증요청을 승인했습니다.").build();
    }


    public CommonResponseDto unapprovedMate(Long mateCid, UnapprovedMateDto unapprovedMateDto) {
        MateEntity mate = mateRepository.findById(mateCid).orElseThrow();
        mate.setMateStatus(MateStatus.FAILED);
        String messageContent = mate.getName()+"님의 메이트 인증이 거부되었습니다! 사유를 확인하시고, 재신청해주세요." +
                " 미승인 사유 : "+unapprovedMateDto.getUnapprovedReason();
        String phoneNum = mate.getPhoneNumber();
        mateRepository.save(mate);
//        sendMessageService.sendMessage(phoneNum,messageContent);
        return CommonResponseDto.builder().code(200).success(true).message("해당 메이트 인증요청을 미승인했습니다.").build();
    }



    public List<MateDto> findAllMateList() {
        log.info("[GET:MASTER] 관리자의 메이트 조회 요청이 들어왔습니다.");
        List<MateEntity> mateList = mateRepository.findAll();
        return mateList.stream().map(MateMapper.INSTANCE::MateEntityToDTO).toList();
    }

    public MateDetailDto findMate(Long mateCid) {
        log.info("[GET:MASTER] 관리자의 메이트 상세 조회 요청이 들어왔습니다.");
        MateEntity mate = mateRepository.findById(mateCid).orElseThrow();
//        String registrationNumber="";
//        for(int i=0; i<=8;i++){
//            registrationNumber += mate.getRegistrationNum().charAt(i)+"";
//        }
//        registrationNumber += "******";
        return MateDetailDto.builder()
                .mateId(mate.getMateId())
                .phoneNum(mate.getPhoneNumber())
                .email(mate.getEmail())
                .registrationNum(mate.getRegistrationNum()) // 뒷자리 첫번째까지만 공개
                .mateGender(mate.getGender())
                .mateName(mate.getName())
                .mateStatus(mate.getMateStatus())
                .build();
    }

    public CommonResponseDto blacklistingMate(boolean isBlacklisted, Long mateCid) {
        log.info("[PUT:MASTER] 관리자의 사용자 블랙리스트 올리기/내리기 요청이 들어왔습니다.");
            MateEntity mate = mateRepository.findById(mateCid).orElseThrow();
            mate.setBlacklisted(isBlacklisted);
            mateRepository.save(mate);
        return CommonResponseDto.builder().code(200).success(true).message("블랙리스트 요청이 성공적으로 처리됐습니다.").build();
    }


    // 사용자 부분
    public List<UserDto> findAllUserList() {
        log.info("[GET:MASTER] 관리자의 사용자 조회 요청이 들어왔습니다.");
        RolesEntity roles = rolesRepository.findByRolesName("ROLE_USER");
        List<UserEntity> userList = authRepository.findAllByRoles(roles);
        return userList.stream().map(UserMapper.INSTANCE::userEntityToDTO).toList();
    }

    public CommonResponseDto blacklistingUser(boolean isBlacklisted,Long userCid) {
        log.info("[PUT:MASTER] 관리자의 사용자 블랙리스트 올리기/내리기 요청이 들어왔습니다.");
            UserEntity user = authRepository.findById(userCid).orElseThrow();
            user.setBlacklisted(isBlacklisted);
            authRepository.save(user);

        return CommonResponseDto.builder().code(200).success(true).message("블랙리스트 요청이 성공적으로 처리됐습니다.").build();
    }


    public UserDetailDto findUser(Long userCid) {
        log.info("[GET:MASTER] 관리자의 사용자 상세 조회 요청이 들어왔습니다.");
        UserEntity user = authRepository.findById(userCid).orElseThrow();
        return UserDetailDto.builder()
                .userId(user.getUserId())
                .userGender(user.getGender())
                .userName(user.getName())
                .phone(user.getPhoneNumber())
                .email(user.getEmail()).build();

    }
}
