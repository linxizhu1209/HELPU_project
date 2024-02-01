package com.github.backend.web.controller;

import com.github.backend.properties.CoolsmsProperties;
import com.github.backend.service.MasterService;
import com.github.backend.service.SendMessageService;
import com.github.backend.web.dto.ApplyMateDto;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.dto.RegisteredUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
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
@GetMapping("/mate")
public ResponseEntity<List<ApplyMateDto>> viewMateList(){
    log.info("[GET] 메이트 신청 회원 목록 조회 요청 들어왔습니다.");
    List<ApplyMateDto> applyMateDto = masterService.findApplyMateList();
    return ResponseEntity.ok().body(applyMateDto);
}



@Operation(summary = "메이트 상세 확인", description = "메이트 정보를 조회한다")
@GetMapping("/user/{mateCid}")
public ResponseEntity<ApplyMateDto> viewMate(@PathVariable Long mateCid){
    log.info("[GET] 메이트 상세 조회 요청 들어왔습니다");
    ApplyMateDto mateDetail = masterService.findMate(mateCid);
    return ResponseEntity.ok().body(mateDetail);
}

@Operation(summary = "메이트 블랙리스트 전환", description = "메이트 블랙리스트 유무를 체크한다")
@PutMapping("/user")
public CommonResponseDto blackingMate(@RequestParam boolean isBlacklisted,
                                      @RequestParam Long mateCid){
    return masterService.blackingMate(isBlacklisted,mateCid);
}





// 사용자 관리

@Operation(summary = "사용자 리스트 확인", description = "사용자 목록을 조회한다")
@GetMapping("/user")
public ResponseEntity<List<RegisteredUser>> viewUserList(){
    log.info("[GET] 사용자 목록 조회 요청 들어왔습니다");
    List<RegisteredUser> UserList = masterService.findAllUserList();
    return ResponseEntity.ok().body(UserList);
}

@Operation(summary = "사용자 상세 확인", description = "사용자 정보를 조회한다")
@GetMapping("/user/{userCid}")
public ResponseEntity<RegisteredUser> viewUser(@PathVariable Long userCid){
    log.info("[GET] 사용자 상세 조회 요청 들어왔습니다");
    RegisteredUser userDetail = masterService.findUser(userCid);
    return ResponseEntity.ok().body(userDetail);
}

@Operation(summary = "사용자 블랙리스트 전환", description = "사용자 블랙리스트 유무를 체크한다")
@PutMapping("/user")
public CommonResponseDto blackingUser(@RequestParam boolean isBlacklisted,
                                      @RequestParam Long userCid){
    return masterService.blackingUser(isBlacklisted,userCid);
}







}
