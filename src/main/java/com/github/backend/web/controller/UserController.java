package com.github.backend.web.controller;

import com.github.backend.service.auth.UserService;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.dto.users.RequestUpdateDto;
import com.github.backend.web.dto.users.ResponseMyInfoDto;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
@Tag(name="유저 API",description = "유저 관련 api")
public class UserController {
    private final UserService userService;

    @Operation(summary = "유저 정보 조회", description = "유저의 정보를 조회한다.")
    @GetMapping("/userInfo")
    public ResponseEntity<ResponseMyInfoDto> findByUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails){
      ResponseMyInfoDto result = userService.findByUser(customUserDetails);
      return ResponseEntity.ok().body(result) ;
    }

    @Operation(summary = "유저 정보 수정하기", description = "유저 정보를 수정한다.")
    @PutMapping(value = "/userInfo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponseDto userInfo(
            @RequestPart(name = "RequestUpdateDto") @Parameter(schema =@Schema(type = "string", format = "binary")) RequestUpdateDto requestUpdateDto,
            @RequestPart(name = "userProfileImage", required = false) MultipartFile profileImages){

      log.info("[Put] 메이트 정보의 수정 요청이 들어왔습니다");
      CommonResponseDto result = userService.updateInfo(requestUpdateDto, profileImages);

      return result;
    }

}
