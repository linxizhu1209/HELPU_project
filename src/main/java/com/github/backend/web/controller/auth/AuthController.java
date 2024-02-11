package com.github.backend.web.controller.auth;

import com.github.backend.service.auth.AuthService;
import com.github.backend.service.auth.UserService;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.dto.mates.RequestSaveMateDto;
import com.github.backend.web.dto.users.*;
import com.github.backend.web.entity.custom.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
    public CommonResponseDto mateSignup(@RequestBody RequestSaveMateDto requestMateDto){
        log.info("[POST] mate signup controller 진입");
        CommonResponseDto result = authService.mateSignup(requestMateDto);
        return result;
    }

    @Operation(summary = "로그인", description = "사용자가 로그인을 한다.")
    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody RequestLoginDto requestLoginDto, HttpServletResponse httpServletResponse) {
      log.info("login controller 진입");
      return authService.login(requestLoginDto, httpServletResponse);
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if(authentication != null){
        new SecurityContextLogoutHandler().logout(request, response, authentication);
      }
      String token = authentication.getCredentials().toString();
      authService.logout(token);
      return ResponseEntity.ok("로그아웃 되었습니다");
    }
    @Operation(summary = "리프레쉬 토큰 발급")
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader("refresh_token") String token){
      return authService.refresh(token);
    }
}
