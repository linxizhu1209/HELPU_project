package com.github.backend.web.controller;

import com.github.backend.service.MasterService;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.dto.UnapprovedMateDto;
import com.github.backend.web.dto.mates.MateDto;
import com.github.backend.web.dto.RegisteredUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/master")
@Tag(name="관리자 API",description = "관리자가 사용자 및 메이트를 관리하는 API입니다")
@Slf4j
public class MasterController {
    private final MasterService masterService;

@Operation(summary = "메이트 승인하기", description = "메이트로 인증요청한 회원을 승인한다.")
@PutMapping("/approve/{mateCid}")
public CommonResponseDto approveMate(
        @PathVariable Long mateCid){
    log.info("[PUT] 메이트 인증승인 요청 들어왔습니다");
    return masterService.approveMate(mateCid);
}

@Operation(summary = "메이트 미승인하기", description = "메이트로 인증요청한 회원을 미승인한다.")
@PutMapping("/unapproved/{mateCid}")
public CommonResponseDto unapprovedMate(
        @PathVariable Long mateCid,
        UnapprovedMateDto unapprovedMateDto){
    log.info("[PUT] 메이트 인증 미승인 요청 들어왔습니다");
    return masterService.unapprovedMate(mateCid,unapprovedMateDto);
    }




@Operation(summary = "메이트 신청목록 확인", description = "메이트로 회원가입 신청한 회원 목록을 조회한다")
@GetMapping("/mate")
public ResponseEntity<List<MateDto>> viewMateList(){
    log.info("[GET] 메이트 신청 회원 목록 조회 요청 들어왔습니다.");
    List<MateDto> applyMateDto = masterService.findApplyMateList();
    return ResponseEntity.ok().body(applyMateDto);
}



@Operation(summary = "메이트 상세 확인", description = "메이트 정보를 조회한다")
@GetMapping("/mate/{mateCid}")
public ResponseEntity<MateDto> viewMate(@PathVariable Long mateCid){
    log.info("[GET] 메이트 상세 조회 요청 들어왔습니다");
    MateDto mateDetail = masterService.findMate(mateCid);
    return ResponseEntity.ok().body(mateDetail);
}

@Operation(summary = "메이트 블랙리스트 전환", description = "메이트 블랙리스트 유무를 체크한다")
@PutMapping("/mate")
public CommonResponseDto blackingMate(@RequestParam boolean isBlacklisted,
                                      @RequestParam Long mateCid){
    return masterService.blacklistingMate(isBlacklisted,mateCid);
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
    return masterService.blacklistingUser(isBlacklisted,userCid);
}
}
