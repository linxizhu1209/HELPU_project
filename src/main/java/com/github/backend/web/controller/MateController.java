package com.github.backend.web.controller;

import com.github.backend.service.MateService;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.dto.mates.CaringDetailsDto;
import com.github.backend.web.dto.mates.CaringDto;
import com.github.backend.web.dto.mates.MainPageDto;
import com.github.backend.web.dto.mates.MyPageDto;
import com.github.backend.web.dto.users.RequestUpdateDto;
import com.github.backend.web.dto.users.ResponseMyInfoDto;
import com.github.backend.web.entity.custom.CustomMateDetails;
import com.github.backend.web.entity.custom.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mate")
@Slf4j
@Tag(name="메이트 API",description = "메이트의 도움 신청, 도움 조회")
public class MateController {

    private final MateService mateService;

    @Operation(summary ="메이트 메인페이지", description = "메이트의 메인페이지 조회")
    @GetMapping("/mainPage")
    public ResponseEntity mainPage(){
        log.info("[GET] 메이트 메인페이지 조회 요청 들어왔습니다");
        // 신규요청 도움 건의 개수를 보여준다("대기중"상태인 도움 개수)
        MainPageDto mainPageDto = mateService.countWaitingCare();
        return ResponseEntity.ok().body(mainPageDto);
    }

    @Operation(summary ="메이트 마이페이지", description = "메이트의 마이페이지 조회")
    @GetMapping("/mypage")
    public ResponseEntity myPage(@AuthenticationPrincipal CustomMateDetails customMateDetails){
        // 도움 상태에 따른 개수 및 별점을 보여준다
        log.info("[GET] 메이트 마이페이지 조회 요청 들어왔습니다");
        MyPageDto myPageDto = mateService.countCareStatus(customMateDetails);
        return ResponseEntity.ok().body(myPageDto);
    }

    @Operation(summary = "도움 지원하기", description = "진행중인 도움 모집건에 지원한다.")
    @PutMapping("/apply/{careCid}")
    public CommonResponseDto applyCaring(@PathVariable Long careCid,
                                         @AuthenticationPrincipal CustomMateDetails customMateDetails){
        log.info("[Put] 메이트의 도움 지원 요청들어왔습니다.");
        return mateService.applyCaring(careCid,customMateDetails);
    }

    @Operation(summary = "도움 완료하기", description = "도움을 끝내고 도움 완료한다")
    @PutMapping("/finish/{careCid}")
    public CommonResponseDto finishCaring(@PathVariable Long careCid,
                                          @AuthenticationPrincipal CustomMateDetails customMateDetails){
        log.info("[Put] 메이트의 도움완료 요청들어왔습니다.");
        return mateService.finishCaring(careCid,customMateDetails);
    }


    @Operation(summary = "도움 취소하기", description = "지원한 도움을 취소한다")
    @PutMapping("/cancel/{careCid}")
    public CommonResponseDto cancelCaring(@PathVariable Long careCid,
                                          @AuthenticationPrincipal CustomMateDetails customMateDetails){
        log.info("[Put] 메이트의 도움취소 요청들어왔습니다.");
        return mateService.cancelCaring(careCid,customMateDetails);
    }

    @Operation(summary = "도움 결제완료", description = "도움에 대한 결제가 됐을 경우 완료한다")
    @PutMapping("/confirmPayment/{careCid}")
    public CommonResponseDto completePayment(
            @PathVariable Long careCid,
            @RequestParam boolean isCompletedPayment){
        log.info("[Put] 메이트의 도움 결제 완료/미완료 요청들어왔습니다.");
        return mateService.completePayment(careCid,isCompletedPayment);
    }

    @Operation(summary = "지원한 도움 목록 조회", description = "지원한 도움 목록을 조회한다/진행중/완료")
    @GetMapping("/careHistory")
    public ResponseEntity<List<CaringDto>> viewApplyList(
            @RequestParam String careStatus,
            @AuthenticationPrincipal CustomMateDetails customMateDetails
    ){
        log.info("[GET] 메이트의 도움 내역 조회 요청 들어왔습니다");
        List<CaringDto> caringList = mateService.viewApplyList(careStatus,customMateDetails);
        return ResponseEntity.ok().body(caringList);
    }


    @Operation(summary = "대기중인 도움 목록 조회", description = "현재 대기중인 전체 도움 목록을 조회한다")
    @GetMapping("/waitingCarelist")
    public ResponseEntity<List<CaringDto>> viewAllApplyList(){
        log.info("대기중인 도움 목록 조회 요청 들어왔습니다.");
        List<CaringDto> caringList = mateService.viewAllApplyList();
        return ResponseEntity.ok().body(caringList);
    }


    @Operation(summary = "도움 상세정보 조회", description = "도움 상세정보를 조회한다")
    @GetMapping("/detailCare/{careCid}")
    public ResponseEntity<CaringDetailsDto> viewDetailCareList(
            @PathVariable Long careCid,
            @AuthenticationPrincipal CustomMateDetails customMateDetails
    ){
        log.info("[POST] 메이트의 도움 내역 상세 요청들어왔습니다.");
        CaringDetailsDto careDetail = mateService.viewCareDetail(careCid);
        return ResponseEntity.ok().body(careDetail);
    }

//---------------------------------------------------------------------------

    @Operation(summary = "메이트 정보 조회", description = "메이트의 정보를 조회한다.")
    @GetMapping("/mateInfo")
    public ResponseMyInfoDto findByMateInfo(@AuthenticationPrincipal CustomMateDetails customMateDetails){
        ResponseMyInfoDto result = mateService.findByMate(customMateDetails);
        return result;
    }

    @Operation(summary = "메이트 정보 수정하기", description = "메이트 정보를 수정한다.")
    @PutMapping(value = "/mateInfo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponseDto mateInfo(
            @Valid @RequestPart(name = "requestUpdateDto") RequestUpdateDto requestUpdateDto,
            @RequestPart(name = "mateProfileImage", required = false) MultipartFile profileImages){

        log.info("[Put] 메이트 정보의 수정 요청이 들어왔습니다");
      CommonResponseDto result = mateService.updateInfo(requestUpdateDto, profileImages);

      return result;
    }

}
