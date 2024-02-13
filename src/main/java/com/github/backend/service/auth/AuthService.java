package com.github.backend.service.auth;

import com.github.backend.config.security.JwtTokenProvider;
import com.github.backend.repository.AuthRepository;
import com.github.backend.repository.MateRepository;
import com.github.backend.repository.RolesRepository;

import com.github.backend.service.exception.NotFoundException;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.dto.mates.RequestSaveMateDto;
import com.github.backend.web.dto.users.*;
import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.RolesEntity;
import com.github.backend.web.entity.UserEntity;
import com.github.backend.web.entity.enums.Gender;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final RedisTemplate<String ,String> redisTemplate;
    private final AuthRepository authRepository;
    private final MateRepository mateRepository;
    private final RolesRepository rolesRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    private final String BEARER_TYPE = "Bearer";
    /**
     * UsernamePasswordAuthenticationToken을 통한 Spring Security인증 진행
     * 이후 tokenService에 userId값을 전달하여 토큰 생성
     * @param requestDTO
     * @return response
     */

    @Transactional
    public ResponseEntity<?> login(RequestLoginDto requestDTO, HttpServletResponse httpServletResponse) {
        UserEntity users = authRepository.findByUserId(requestDTO.getUserId()).isPresent() == false ? null : authRepository.findByUserId(requestDTO.getUserId()).get();
        MateEntity mates = mateRepository.findByMateId(requestDTO.getUserId()).isPresent() == false ? null : mateRepository.findByMateId(requestDTO.getUserId()).get();
        if(users == null && mates == null){
          throw new NotFoundException("아이디 혹은 비밀번호가 틀렸습니다.");
        }
        try{
          // 1. ID(email)/PW 기반으로 AuthenticationToken 생성
          UsernamePasswordAuthenticationToken authenticationToken = requestDTO.toAuthentication();
          // 2. 실제 검증 로직
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);

          // 3. 로그아웃 토큰이 있는 경우 삭제
          if (redisTemplate.opsForValue().get("logout: " + requestDTO.getUserId()) != null) {
            redisTemplate.delete("logout: " + requestDTO.getUserId());
          }

          String accessToken = "";
          String refreshToken = "";

          // 4. 유저, 메이트 인증 정보를 기반으로 JWT토큰 생성
          if(users != null){
            String isDeletedUser = users.getIsDeleted();
            if(isDeletedUser != null && isDeletedUser.equals("deleted"))
              throw new NotFoundException("해당 사용자는 탈퇴한 계정입니다.");

            if(!passwordEncoder.matches(requestDTO.getPassword(), users.getPassword()))
              throw new NotFoundException("아이디 혹은 비밀번호가 틀렸습니다.");

            accessToken = jwtTokenProvider.createAccessToken(1, users.getUserId());
            refreshToken = jwtTokenProvider.createRefreshToken(1, users.getUserId());

          }else if(mates != null){
            String isDeletedMate = mates.getIsDeleted();
            if(isDeletedMate != null && isDeletedMate.equals("deleted"))
              throw new NotFoundException("해당 메이트는 탈퇴한 계정입니다.");

            if(!passwordEncoder.matches(requestDTO.getPassword(), mates.getPassword()))
              throw new NotFoundException("아이디 혹은 비밀번호가 틀렸습니다.");

            accessToken = jwtTokenProvider.createAccessToken(2, mates.getMateId());
            refreshToken = jwtTokenProvider.createRefreshToken(2, mates.getMateId());
          }

          redisTemplate.opsForValue().set(requestDTO.getUserId(), accessToken, Duration.ofSeconds(1800));
          redisTemplate.opsForValue().set("RF: " + requestDTO.getUserId(), refreshToken, Duration.ofHours(1L));

          httpServletResponse.addCookie(new Cookie("refresh_token", refreshToken));

          Map<String, String> response = new HashMap<>();
          response.put("http_status", HttpStatus.OK.toString());
          response.put("message", "로그인 되었습니다");
          response.put("token_type", BEARER_TYPE);
          response.put("access_token", accessToken);
          response.put("refresh_token", refreshToken);
          return ResponseEntity.ok(response);
        }catch (BadCredentialsException e) {
          e.printStackTrace();
          Map<String, String> response = new HashMap<>();
          response.put("message", "잘못된 자격 증명입니다");
          response.put("http_status", HttpStatus.UNAUTHORIZED.toString());
          return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (UsernameNotFoundException e) {
          e.printStackTrace();
          Map<String, String> response = new HashMap<>();
          response.put("message", "가입되지 않은 회원입니다");
          response.put("http_status", HttpStatus.NOT_FOUND.toString());
          return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (Exception e) {
          e.printStackTrace();
          Map<String, String> response = new HashMap<>();
          response.put("message", "알 수 없는 오류가 발생했습니다");
          response.put("http_status", HttpStatus.INTERNAL_SERVER_ERROR.toString());
          return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    @Transactional
    public CommonResponseDto userSignup(RequestSaveUserDto requestSaveUserDto) {
      if (authRepository.existsByUserId(requestSaveUserDto.getUserId())) {
        throw new IllegalStateException("이미 존재하는 아이디입니다.");
      }
      requestSaveUserDto.setPassword(passwordEncoder.encode(requestSaveUserDto.getPassword()));

      RolesEntity roles = rolesRepository.findById(requestSaveUserDto.getRoles()).orElseThrow(() -> new NotFoundException("권한이 존재하지 않습니다."));

      Gender gender;
      if (requestSaveUserDto.getGender().equals("남자"))
        gender = Gender.MEN;
      else
        gender = Gender.WOMEN;

      UserEntity user = UserEntity.builder()
              .userId(requestSaveUserDto.getUserId())
              .password(requestSaveUserDto.getPassword())
              .email(requestSaveUserDto.getEmail())
              .name(requestSaveUserDto.getName())
              .phoneNumber(requestSaveUserDto.getPhoneNumber())
              .address(requestSaveUserDto.getAddress())
              .gender(gender)
              .roles(roles)
              .isDeleted(null)
              .build();
      log.info("[build] user = " + user);
      authRepository.save(user);

      return CommonResponseDto.builder()
              .code(200)
              .message("유저 회원가입이 완료되었습니다.")
              .success(true)
              .build();
    }

    @Transactional
    public CommonResponseDto mateSignup(RequestSaveMateDto requestMateDto) {
        if (mateRepository.existsByMateId(requestMateDto.getMateId())) {
          throw new RuntimeException("이미 존재하는 아이디입니다.");
        }
        requestMateDto.setPassword(passwordEncoder.encode(requestMateDto.getPassword()));

        RolesEntity roles = rolesRepository.findById(requestMateDto.getRoles()).orElseThrow(() -> new NotFoundException("권한이 존재하지 않습니다."));

        Gender gender;
        if (requestMateDto.getGender().equals("남자"))
          gender = Gender.MEN;
        else
          gender = Gender.WOMEN;

        MateEntity mate = MateEntity.builder()
                .mateId(requestMateDto.getMateId())
                .password(requestMateDto.getPassword())
                .email(requestMateDto.getEmail())
                .name(requestMateDto.getName())
                .phoneNumber(requestMateDto.getPhoneNumber())
                .address(requestMateDto.getAddress())
                .gender(gender)
                .roles(roles)
                .registrationNum(requestMateDto.getRegistrationNum())
                .isDeleted(null)
                .build();

        log.info("[build] mate = " + mate);
        mateRepository.save(mate);

       return CommonResponseDto.builder()
                .code(200)
                .message("메이트 회원가입이 완료되었습니다.")
                .success(true)
                .build();
    }

    // 로그아웃
    @Transactional
    public void logout(String token){
      redisTemplate.opsForValue().set("logout : "+ jwtTokenProvider.getIdByToken(token), "logout", Duration.ofSeconds(1800));
      redisTemplate.delete(jwtTokenProvider.getIdByToken(token));
      redisTemplate.delete("RF: " + jwtTokenProvider.getIdByToken(token));
    }

    // refresh token 재발급
    @Transactional
    public ResponseEntity<?> refresh(String token){
      String id = jwtTokenProvider.getIdByToken(token);
      String accessToken = checkByToken(id);
      if(redisTemplate.opsForValue().get("RF: " + id) != null){
        redisTemplate.opsForValue().set(id, accessToken, Duration.ofSeconds(6000));
        Map<String, String> response = new HashMap<>();
        response.put("access_token", accessToken);
        response.put("http_status", HttpStatus.CREATED.toString());
        return ResponseEntity.ok(response);
      }
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 유저입니다.");
    }

    // --------------------------------함수 정의하는 곳--------------------------------
    /*
    * 토큰으로 user 를 체크
    */
    public String checkByToken(String id){
      UserEntity users = authRepository.findByUserId(id).isPresent() == false ? null : authRepository.findByUserId(id).get();
      MateEntity mates = mateRepository.findByMateId(id).isPresent() == false ? null : mateRepository.findByMateId(id).get();
      String accessToken = "";
      if(users != null){
        accessToken = jwtTokenProvider.createAccessToken(1, users.getUserId());
      }else{
        accessToken = jwtTokenProvider.createAccessToken(2, mates.getMateId());
      }
      return accessToken;
    }

}
