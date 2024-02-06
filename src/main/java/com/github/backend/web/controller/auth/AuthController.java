package com.github.backend.web.controller.auth;

import com.github.backend.service.auth.AuthService;
import com.github.backend.service.auth.UserService;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.dto.mates.RequestSaveMateDto;
import com.github.backend.web.dto.users.*;
import com.github.backend.web.entity.custom.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "접속관련 api", description = "유저 접속 관련")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    @Operation(summary = "유저 회원가입 요청", description = "유저 회원가입을 한다.")
    @PostMapping("/user/signup")
    public CommonResponseDto userSignUp(@RequestBody RequestSaveUserDto requestSaveUserDto){
        log.info("[POST] user signup controller 진입");
        CommonResponseDto result = authService.userSignup(requestSaveUserDto);
        return result;
    }

    @Operation(summary = "메이트 회원가입 요청", description = "메이트 회원가입을 한다.")
    @PostMapping("/mate/signup")
    public ResponseEntity<String> mateSignup(@RequestBody RequestSaveMateDto requestMateDto){
        log.info("[POST] mate signup controller 진입");
        authService.mateSignup(requestMateDto);
        return new ResponseEntity<>("메이트 회원가입이 완료되었습니다.", HttpStatus.OK);
    }

    @Operation(summary = "로그인", description = "사용자가 로그인을 한다.")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> userLogin(@RequestBody RequestLoginDto requestLoginDto, HttpServletResponse httpServletResponse) {
      log.info("login controller 진입");
      TokenDto tokenDTO = authService.login(requestLoginDto);
      ResponseCookie responseCookie = ResponseCookie
              .from("refresh_token", tokenDTO.getRefreshToken())
              .httpOnly(true)
              .secure(true)
              .sameSite("None")
              .maxAge(tokenDTO.getDuration())
              .path("/")
              .build();

  //      httpServletResponse.setHeader("Authorization", tokenDTO.getAccessToken());
      httpServletResponse.setHeader("Set-Cookie", responseCookie.toString());

      log.info("access token : {}", tokenDTO.getAccessToken());
      log.info("refresh token: {}", tokenDTO.getRefreshToken());
      return ResponseEntity.ok(tokenDTO);
    }

    @Operation(summary = "토큰 갱신")
    @PostMapping("/refreshToken")
    public ResponseEntity<TokenDto> refreshToken(@RequestBody RequestTokenDto tokenDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
      TokenDto tokenDto = authService.refresh(tokenDTO, customUserDetails);
      return ResponseEntity.ok(tokenDto);
    }

    @Operation(summary = "해당 아이디 유저 로그인 정보")
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseUserDto> getUserInfo(@PathVariable String userId) {
      return ResponseEntity.ok(userService.getLoginUserInfo(userId));
    }

    @Operation(summary = "현재 로그인 정보")
    @GetMapping("/myInfo")
    public ResponseEntity<ResponseUserDto> getUserInfo() {
      return ResponseEntity.ok(userService.getLoginUserInfo());
    }

}
