package com.github.backend.web.controller;

import com.github.backend.properties.CoolsmsProperties;
import com.github.backend.service.MasterService;
import com.github.backend.service.SendMessageService;
import com.github.backend.web.dto.ApplyMateDto;
import com.github.backend.web.dto.CommonResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/master")
@Tag(name="관리자 API",description = "관리자가 사용자 및 메이트를 관리하는 API입니다")
@Slf4j
public class MasterController {
    private final MasterService masterService;

@Operation(summary = "메이트 인증하기", description = "메이트로 회원가입 신청한 회원을 인증해준다.")
@PutMapping("")
public CommonResponseDto registerMate(
        @RequestParam("mateId") Long mateId,
        @RequestParam("register") boolean isAccept){
    log.info("[POST] 메이트 인증완료/거부 요청 들어왔습니다");
    return masterService.registerMate(mateId,isAccept);
}


@Operation(summary = "메이트 신청목록 확인", description = "메이트로 회원가입 신청한 회원 목록을 조회한다")
@GetMapping("")
public ResponseEntity<List<ApplyMateDto>> viewMateList(){
    log.info("[GET] 메이트 신청 회원 목록 조회 요청 들어왔습니다.");
    List<ApplyMateDto> applyMateDto = masterService.findApplyMateList();
    return ResponseEntity.ok().body(applyMateDto);
}



}
