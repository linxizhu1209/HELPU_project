package com.github.backend.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.github.backend.config.security.util.AESUtil;
import com.github.backend.repository.AuthRepository;
import com.github.backend.repository.MateRepository;
import com.github.backend.repository.RolesRepository;
import com.github.backend.service.exception.CommonException;
import com.github.backend.service.mapper.MateCaringMapper;
import com.github.backend.service.mapper.MateMapper;
import com.github.backend.service.mapper.UserMapper;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.dto.master.UnapprovedMateDto;
import com.github.backend.web.dto.master.UserDetailDto;
import com.github.backend.web.dto.master.UserListDto;
import com.github.backend.web.dto.master.MateDetailDto;
import com.github.backend.web.dto.master.MateDto;
import com.github.backend.web.dto.mates.CaringDto;
import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.ProfileImageEntity;
import com.github.backend.web.entity.RolesEntity;
import com.github.backend.web.entity.UserEntity;
import com.github.backend.web.entity.enums.ErrorCode;
import com.github.backend.web.entity.enums.MateStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MasterService {
    
    private final MateRepository mateRepository;
    private final SendMessageService sendMessageService;
    private final AuthRepository authRepository;
    private final RolesRepository rolesRepository;
    private final AESUtil aesUtil;

    public CommonResponseDto approveMate(Long mateCid) {
        MateEntity mate = mateRepository.findById(mateCid).orElseThrow(()->new CommonException("해당 메이트를 찾을 수 없습니다.", ErrorCode.FAIL_RESPONSE));
        mate.setMateStatus(MateStatus.COMPLETE);
        String messageContent = mate.getName()+"님의 메이트 인증이 허가되었습니다! 로그인할 수 있습니다. ";
        String phoneNum = mate.getPhoneNumber();
        mateRepository.save(mate);
//        sendMessageService.sendMessage(phoneNum,messageContent);
        return CommonResponseDto.builder().code(200).success(true).message("해당 메이트 인증요청을 승인했습니다.").build();
    }

    public CommonResponseDto unapprovedMate(Long mateCid, UnapprovedMateDto unapprovedMateDto) {
        MateEntity mate = mateRepository.findById(mateCid).orElseThrow(()->new CommonException("해당 메이트를 찾을 수 없습니다.", ErrorCode.FAIL_RESPONSE));
        mate.setMateStatus(MateStatus.FAILED);
        String messageContent = mate.getName()+"님의 메이트 인증이 거부되었습니다! 사유를 확인하시고, 재신청해주세요." +
                " 미승인 사유 : "+unapprovedMateDto.getUnapprovedReason();
        String phoneNum = mate.getPhoneNumber();
        mateRepository.save(mate);
//        sendMessageService.sendMessage(phoneNum,messageContent);
        return CommonResponseDto.builder().code(200).success(true).message("해당 메이트 인증요청을 미승인했습니다.").build();
    }


    public List<MateDto> findAllMateList() {
        List<MateEntity> mateList = mateRepository.findAll();
        List<ProfileImageEntity> mateImageList = mateList.stream().map(mateEntity -> mateEntity.getProfileImage())
                .map(profileImage -> Optional.ofNullable(profileImage).orElse(null)).toList();
        // 도움을 신청한 유저에 해당하는 이미지 하나씩 불러와서 하나씩 넣어주기
        List<MateDto> mateListDtos = mateList.stream().map(MateMapper.INSTANCE::MateEntityToDTO).toList();
        for (int i = 0; i < mateListDtos.size(); i++) {
            ProfileImageEntity profileImage = mateImageList.get(i);
            if (profileImage != null) {
                mateListDtos.get(i).setImageName(mateImageList.get(i).getFileExt());
                mateListDtos.get(i).setImageAddress(mateImageList.get(i).getFileUrl());
            } else {
                mateListDtos.get(i).setImageName("default");
                mateListDtos.get(i).setImageAddress(null);
            }
        }

     return mateListDtos;
    }


    public MateDetailDto findMate(Long mateCid) {
        MateEntity mate = mateRepository.findById(mateCid).orElseThrow(()->new CommonException("해당 메이트를 찾을 수 없습니다.", ErrorCode.FAIL_RESPONSE));

        String decryptNum = maskRegistrationNumber(aesUtil.decrypt(mate.getRegistrationNum()));
        ProfileImageEntity profileImage =mate.getProfileImage();
        String imageAddress = null;
        String imageName = "Default";

        if (profileImage != null) {
            imageAddress = profileImage.getFileUrl();
            imageName = profileImage.getFileExt();
        }

        return MateDetailDto.builder()
                .mateId(mate.getMateId())
                .phoneNum(mate.getPhoneNumber())
                .email(mate.getEmail())
                .registrationNum(decryptNum) // 뒷자리 첫번째까지만 공개
                .mateGender(mate.getGender())
                .mateName(mate.getName())
                .mateStatus(mate.getMateStatus())
                .imageAddress(imageAddress)
                .imageName(imageName)
                .build();
    }


    public CommonResponseDto blacklistingMate(boolean isBlacklisted, Long mateCid) {
            MateEntity mate = mateRepository.findById(mateCid).orElseThrow(()->new CommonException("해당 메이트를 찾을 수 없습니다.", ErrorCode.FAIL_RESPONSE));
            mate.setBlacklisted(isBlacklisted);
            mateRepository.save(mate);
        return CommonResponseDto.builder().code(200).success(true).message("블랙리스트 요청이 성공적으로 처리됐습니다.").build();
    }

    /**
     * 밑에부터 사용자 관련 코드
     */

    public List<UserListDto> findAllUserList() {
        RolesEntity roles = rolesRepository.findByRolesName("ROLE_USER");
        List<UserEntity> userList = authRepository.findAllByRoles(roles);
        List<ProfileImageEntity> userImageList = userList.stream().map(userEntity -> userEntity.getProfileImage())
                .map(profileImage -> Optional.ofNullable(profileImage).orElse(null)).toList();
        // 도움을 신청한 유저에 해당하는 이미지 하나씩 불러와서 하나씩 넣어주기
        List<UserListDto> userListDtos = userList.stream().map(UserMapper.INSTANCE::userEntityToDTO).toList();
        for (int i = 0; i < userListDtos.size(); i++) {
            ProfileImageEntity profileImage = userImageList.get(i);
            if (profileImage != null) {
                userListDtos.get(i).setImageName(userImageList.get(i).getFileExt());
                userListDtos.get(i).setImageAddress(userImageList.get(i).getFileUrl());
            } else {
                userListDtos.get(i).setImageName("default");
                userListDtos.get(i).setImageAddress(null);
            }
        }

        return userListDtos;
    }

    public CommonResponseDto blacklistingUser(boolean isBlacklisted,Long userCid) {
            UserEntity user = authRepository.findById(userCid).orElseThrow(()->new CommonException("해당 사용자를 찾을 수 없습니다.", ErrorCode.FAIL_RESPONSE));
            user.setBlacklisted(isBlacklisted);
            authRepository.save(user);

        return CommonResponseDto.builder().code(200).success(true).message("블랙리스트 요청이 성공적으로 처리됐습니다.").build();
    }


    public UserDetailDto findUser(Long userCid) {
        UserEntity user = authRepository.findById(userCid).orElseThrow(()-> new CommonException("해당 사용자를 찾을 수 없습니다.", ErrorCode.FAIL_RESPONSE));
        ProfileImageEntity profileImage =user.getProfileImage();
        String imageAddress = null;
        String imageName = "Default";

        if (profileImage != null) {
            imageAddress = profileImage.getFileUrl();
            imageName = profileImage.getFileExt();
        }


        return UserDetailDto.builder()
                .userId(user.getUserId())
                .userGender(user.getGender())
                .userName(user.getName())
                .phone(user.getPhoneNumber())
                .email(user.getEmail())
                .imageAddress(imageAddress)
                .imageName(imageName).build();
    }

    /*-------------------------------함수 추가-----------------------------------*/
    private String maskRegistrationNumber(String decryptedNum) {
      if (decryptedNum != null && decryptedNum.length() >= 8) {
        // 뒷자리 첫 번째 이후의 문자열을 '*'로 대체
        String maskedPart = decryptedNum.substring(8).replaceAll(".", "*");
        return decryptedNum.substring(0, 8) + maskedPart;
      }
      return decryptedNum;
    }
}
