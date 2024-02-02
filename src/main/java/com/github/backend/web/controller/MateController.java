package com.github.backend.web.controller;

import com.github.backend.service.MateService;
import com.github.backend.web.dto.AppliedCaringDto;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.dto.MateDto;
import com.github.backend.web.dto.request.RequestMateDto;
import com.github.backend.web.entity.custom.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "도움 지원하기", description = "진행중인 도움 모집건에 지원한다.")
    @PostMapping("")
    public CommonResponseDto applyCaring(@PathVariable Long careId,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.info("[GET] 메이트 로그인 후 메인화면 조회 요청 들어왔습니다");
        return mateService.applyCaring(careId,customUserDetails);
    }

    @Operation(summary = "지원한 도움 목록 조회", description = "지원한 도움 목록을 조회한다/진행중/완료")
    @GetMapping("/applyCarelist")
    public ResponseEntity<List<AppliedCaringDto>> viewApplyList(
            @RequestParam String careStatus,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        List<AppliedCaringDto> applyCaringList = mateService.viewApplyList(careStatus,customUserDetails);
        return ResponseEntity.ok().body(applyCaringList);
    }


    @Operation(summary = "전체 도움 목록 조회", description = "현재 대기중인 전체 도움 목록을 조회한다")
    @GetMapping("/all-applyCarelist")
    public ResponseEntity<List<AppliedCaringDto>> viewAllApplyList(){
        List<AppliedCaringDto> applyCaringList = mateService.viewAllApplyList();
        return ResponseEntity.ok().body(applyCaringList);
    }



    @Operation(summary = "도움 완료하기", description = "도움을 끝내고 도움 완료한다")
    @PutMapping("/finish/{careCid}")
    public CommonResponseDto finishCaring(@PathVariable Long careCid){
        return mateService.finishCaring(careCid);
    }

//
//    @Operation(summary = "도움 취소하기", description = "지원한 도움을 취소한다")
//    @PutMapping("/cancel/{careCid}")
//    public CommonResponseDto cancelCaring(@RequestParam Long careCid){
//        return mateService.cancelCaring(careCid);
//
//    } 메이트가 도움 취소한 내역을 따로 관리할 수 있는 테이블 생성되면 주석 해제

    @Operation(summary = "메이트 정보 수정하기", description = "메이트 정보를 수정한다.")
    @PutMapping(value = "/mateInfo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponseDto mateInfo(
            @RequestPart(name = "mateDto") @Parameter(schema =@Schema(type = "string", format = "binary")) RequestMateDto requestMateDto,
            @RequestPart(name = "mateProfileImage", required = false) MultipartFile profileImages){

        log.info("[Put] 메이트 정보의 수정 요청이 들어왔습니다");
      CommonResponseDto result = mateService.updateInfo(requestMateDto, profileImages);

      return result;
    }

}
