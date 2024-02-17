package com.github.backend.service;

import com.github.backend.repository.*;
import com.github.backend.service.exception.CommonException;
import com.github.backend.service.exception.InvalidValueException;
import com.github.backend.service.exception.NotFoundException;
import com.github.backend.service.mapper.MateCaringMapper;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.dto.mates.CaringDetailsDto;
import com.github.backend.web.dto.mates.CaringDto;
import com.github.backend.web.dto.mates.MainPageDto;
import com.github.backend.web.dto.mates.MyPageDto;
import com.github.backend.web.dto.users.RequestUpdateDto;
import com.github.backend.web.dto.users.ResponseMyInfoDto;
import com.github.backend.web.entity.*;
import com.github.backend.web.entity.custom.CustomMateDetails;
import com.github.backend.web.entity.enums.CareStatus;
import com.github.backend.web.entity.enums.ErrorCode;
import com.github.backend.web.entity.enums.MateCareStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

import static com.github.backend.web.dto.apply.UserDto.careEntityToUserDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class MateService {


    private final PasswordEncoder passwordEncoder;
    private final ServiceApplyRepository careRepository;
    private final MateRepository mateRepository;
    private final MateCareHistoryRepository mateCareHistoryRepository;
    private final RatingRepository ratingRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final AuthRepository authRepository;


    @Transactional
    public CommonResponseDto applyCaring(Long careId, CustomMateDetails customMateDetails) {
        Long mateId = customMateDetails.getMate().getMateCid();
        CareEntity care = careRepository.findById(careId).orElseThrow(()->new CommonException("요청하신 도움신청건을 찾을 수 없습니다.", ErrorCode.FAIL_RESPONSE));
        MateEntity mate = mateRepository.findById(mateId).orElseThrow(()->new CommonException("해당 메이트를 찾을 수 없습니다.", ErrorCode.FAIL_RESPONSE));
        // 만약 동일한 날짜 같은 시간대에 이미 신청한 도움이 있다면 신청불가능하게끔하기
        List<MateCareHistoryEntity> mateCareHistoryEntities = mateCareHistoryRepository.findAllByMateAndMateCareStatus(mate, MateCareStatus.IN_PROGRESS);
        List<CareEntity> careEntities = mateCareHistoryEntities.stream().map(MateCareHistoryEntity::getCare).toList();
        for (CareEntity caring : careEntities) {
            if (caring.getCareDate().equals(care.getCareDate())) {
            throw new CommonException("해당날짜에 이미 지원한 도움이 있습니다.", ErrorCode.BAD_REQUEST_RESPONSE);
            }
        }
        UserEntity user = authRepository.findById(care.getUser().getUserCid()).orElseThrow(()->new CommonException("지원하신 도움신청건의 사용자를 찾을 수 없습니다.", ErrorCode.FAIL_RESPONSE));
        care.setMate(mate);
        care.setCareStatus(CareStatus.IN_PROGRESS);
//        String messageContent = care.getContent + "가 매칭되었습니다!";
//        String phoneNum = care.getUser().getPhoneNumber();
//        sendMessageService.sendMessage(phoneNum,messageContent);

        MateCareHistoryEntity mateCareHistory = MateCareHistoryEntity.builder().mate(mate).care(care).mateCareStatus(MateCareStatus.IN_PROGRESS).build();
        mateCareHistoryRepository.save(mateCareHistory);
        careRepository.save(care);

        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
                .mate(mate)
                .user(user)
                .build();

        chatRoomRepository.save(chatRoomEntity);


        return CommonResponseDto.builder().code(200).success(true).message("도움 지원이 완료되었습니다!").build();
    }

    public CommonResponseDto finishCaring(Long careCid, CustomMateDetails customMateDetails) {
        CareEntity care = careRepository.findById(careCid).orElseThrow(()->new CommonException("요청하신 도움을 찾을 수 없습니다.", ErrorCode.FAIL_RESPONSE));
        care.setCareStatus(CareStatus.HELP_DONE);
        MateCareHistoryEntity mateCareHistory = mateCareHistoryRepository.findByCareAndMate(care, customMateDetails.getMate());
        mateCareHistory.setMateCareStatus(MateCareStatus.HELP_DONE);
        careRepository.save(care);
        mateCareHistoryRepository.save(mateCareHistory);
        return CommonResponseDto.builder().code(200).success(true).message("도움이 완료되었습니다!").build();
    }


    public CommonResponseDto cancelCaring(Long careCid, CustomMateDetails customMateDetails) {
        CareEntity care = careRepository.findById(careCid).orElseThrow(()->new CommonException("요청하신 도움을 찾을 수 없습니다.", ErrorCode.FAIL_RESPONSE));
        care.setCareStatus(CareStatus.WAITING);
        care.setMate(null);
        MateCareHistoryEntity mateCareHistory = mateCareHistoryRepository.findByCareAndMate(care, customMateDetails.getMate());
        mateCareHistory.setMateCareStatus(MateCareStatus.CANCEL);
        careRepository.save(care);
        mateCareHistoryRepository.save(mateCareHistory);
//        String messageContent = care.getContent + "매칭된 메이트가 도움을 취소하였습니다! 아쉽지만 새로운 메이트를 기다려주세요!";
//        String phoneNum = care.getUser().getPhoneNumber();
//        sendMessageService.sendMessage(phoneNum,messageContent);


        return CommonResponseDto.builder().code(200).success(true)
                .message("도움을 성공적으로 취소했습니다!").build();
    }

    ;

    @Transactional
    public List<CaringDto> viewApplyList(String careStatus,CustomMateDetails customMateDetails) {
        MateEntity mate = mateRepository.findById(customMateDetails.getMate().getMateCid()).orElseThrow(()->new CommonException("해당 메이트를 찾을 수 없습니다.", ErrorCode.FAIL_RESPONSE));
        MateCareStatus mateCareStatus;
            if (careStatus.equalsIgnoreCase("IN_PROGRESS")) {
                mateCareStatus = MateCareStatus.IN_PROGRESS;
            } else if (careStatus.equalsIgnoreCase("HELP_DONE")){
            mateCareStatus = MateCareStatus.HELP_DONE;
            }
            else if(careStatus.equalsIgnoreCase("cancel")) {
            mateCareStatus = MateCareStatus.CANCEL;
            }
            else {
            throw new InvalidValueException("상태를 잘못입력하였습니다. 다시 입력해주세요.");
            }
        List<MateCareHistoryEntity> mateCareHistoryList = mateCareHistoryRepository.findAllByMateAndMateCareStatus(mate, mateCareStatus);
        List<CareEntity> careList = mateCareHistoryList.stream().map(MateCareHistoryEntity::getCare).toList();
            return careList.stream().map(MateCaringMapper.INSTANCE::CareEntityToDTO).toList();

        }



    // 신규요청 내역 조회하기(대기중인 도움들 전체 조회)
    public List<CaringDto> viewAllApplyList() {
        List<CareEntity> careList = careRepository.findAllByCareStatus(CareStatus.WAITING);
        return careList.stream().map(MateCaringMapper.INSTANCE::CareEntityToDTO).toList();
    }

    @Transactional
    public CommonResponseDto updateInfo(RequestUpdateDto requestUpdateDto, MultipartFile profileImages) {

        if (!mateRepository.existsById(requestUpdateDto.getCid())) {
            throw new CommonException("아이디가 존재하지 않습니다.", ErrorCode.FAIL_RESPONSE);
        }

        MateEntity mate = mateRepository.findById(requestUpdateDto.getCid()).orElseThrow(() -> new CommonException("해당 메이트를 찾을 수 없습니다.", ErrorCode.FAIL_RESPONSE));
        if (requestUpdateDto.getPassword() != null) {
            requestUpdateDto.setPassword(passwordEncoder.encode(requestUpdateDto.getPassword()));
            mate.setEmail(requestUpdateDto.getEmail());
            mate.setPassword(requestUpdateDto.getPassword());
            mate.setPhoneNumber(requestUpdateDto.getPhoneNumber());
        } else {
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

    public ResponseMyInfoDto findByMate(CustomMateDetails customMateDetails) {
        MateEntity mate = mateRepository.findById(customMateDetails.getMate().getMateCid()).orElseThrow(() -> new CommonException("해당 메이트를 찾을 수 없습니다.", ErrorCode.FAIL_RESPONSE));
        return ResponseMyInfoDto.builder()
                .cid(mate.getMateCid())
                .name(mate.getName())
                .id(mate.getMateId())
                .email(mate.getEmail())
                .phoneNumber(mate.getPhoneNumber())
                .build();
    }

    public CaringDetailsDto viewCareDetail(Long careCid) {
        CareEntity care = careRepository.findById(careCid).orElseThrow(()->new CommonException("요청하신 도움신청건을 찾을 수 없습니다.", ErrorCode.FAIL_RESPONSE));
        return CaringDetailsDto.builder().date(care.getCareDate()).startTime(care.getCareDateTime())
                .finishTime(care.getRequiredTime()).arrivalLoc(care.getArrivalLoc())
                .gender(care.getGender()).cost(care.getCost()).careCid(careCid)
                .content(care.getContent()).userId(care.getUser().getUserId()).build();
    }

    public MainPageDto countWaitingCare() {
        int count = careRepository.countByCareStatus(CareStatus.WAITING);
        return new MainPageDto(count);
    }

    public MyPageDto countCareStatus(CustomMateDetails customMateDetails) {
        MateEntity mate = customMateDetails.getMate();
        int waitingCount = careRepository.countByCareStatus(CareStatus.WAITING);
        int inProgressCount = mateCareHistoryRepository.countByMateCareStatusAndMate(MateCareStatus.IN_PROGRESS, mate);
        int finishCount = mateCareHistoryRepository.countByMateCareStatusAndMate(MateCareStatus.HELP_DONE, mate);
        int cancelCount = mateCareHistoryRepository.countByMateCareStatusAndMate(MateCareStatus.CANCEL, mate);
        Optional<MateRatingEntity> mateRatingOptional = ratingRepository.findByMate(mate);
        double mateRating = mateRatingOptional.map(mateRatingEntity ->
                        mateRatingEntity.getTotalRating() / (double) mateRatingEntity.getRatingCount())
                .orElse(0.0); //
        return MyPageDto.builder().waitingCount(waitingCount).
                inProgressCount(inProgressCount).finishCount(finishCount)
                .cancelCount(cancelCount).mateRating(mateRating).build();
    }

    public CommonResponseDto completePayment(Long careCid, boolean isCompletedPayment) {
      CareEntity care = careRepository.findById(careCid).orElseThrow(()->new CommonException("요청하신 도움신청건을 찾을 수 없습니다.", ErrorCode.FAIL_RESPONSE));
      if (isCompletedPayment) {
        care.setCareStatus(CareStatus.COMPLETE_PAYMENT);
      } else {
        care.setCareStatus(CareStatus.INCOMPLETE_PAYMENT);
//        String messageContent = care.getContent + "의 결제가 완료되지않아 메이트가 기다리고있어요ㅠㅠ 입금을 완료해주세요!";
//        String phoneNum = care.getUser().getPhoneNumber();
//        sendMessageService.sendMessage(phoneNum,messageContent);
      }
      careRepository.save(care);
      return CommonResponseDto.builder().code(200).success(true).message("결제 상태가 성공적으로 변경되었습니다!").build();
    }
}

