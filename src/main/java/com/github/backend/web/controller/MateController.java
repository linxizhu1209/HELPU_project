package com.github.backend.web.controller;

import com.github.backend.service.MateService;
import com.github.backend.web.dto.AppliedCaringDto;
import com.github.backend.web.dto.CommonResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mate")
@Slf4j
@Tag(name="메이트 API",description = "메이트의 도움 신청, 도움 조회")
public class MateController {

    private final MateService mateService;

//    @Operation(summary = "메이트 인증 대기 화면", description = "메이트로 회원가입 신청하고 대기중인 회원의 화면")
    @Operation(summary = "도움 지원하기", description = "진행중인 도움 모집건에 지원한다.")
    @PostMapping("")
    public CommonResponseDto applyCaring(@PathVariable Long careId){
        log.info("[GET] 메이트 로그인 후 메인화면 조회 요청 들어왔습니다");
        return mateService.applyCaring(careId);
    }

    @Operation(summary = "지원한 도움 목록 조회", description = "지원한 도움 목록을 조회한다/진행중/완료")
    @GetMapping("/applyCarelist")
    public ResponseEntity<List<AppliedCaringDto>> viewApplyList(
            @RequestParam String careStatus
    ){
        List<AppliedCaringDto> applyCaringList = mateService.viewApplyList(careStatus);
        return ResponseEntity.ok().body(applyCaringList);
    }


    @Operation(summary = "전체 도움 목록 조회", description = "현재 대기중인 전체 도움 목록을 조회한다")
    @GetMapping("/all-applyCarelist")
    public ResponseEntity<List<AppliedCaringDto>> viewAllApplyList(){
        List<AppliedCaringDto> applyCaringList = mateService.viewAllApplyList();
        return ResponseEntity.ok().body(applyCaringList);
    }



    @Operation(summary = "도움 완료하기", description = "도움을 끝내고 정산까지 됐을 시 도움을 완료한다")
    @PutMapping("/finish/{careCid}")
    public CommonResponseDto finishCaring(@PathVariable Long careCid){
        return mateService.finishCaring(careCid);

    }


    @Operation(summary = "도움 취소하기", description = "지원한 도움을 취소한다")
    @PutMapping("/cancel/{careCid}")
    public CommonResponseDto cancelCaring(@RequestParam Long careCid){
        return mateService.cancelCaring(careCid);

    }



}
