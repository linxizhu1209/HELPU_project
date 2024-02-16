package com.github.backend.service.auth;

import com.github.backend.config.security.JwtTokenProvider;
import com.github.backend.config.security.util.AESUtil;
import com.github.backend.repository.AuthRepository;
import com.github.backend.repository.MateRepository;
import com.github.backend.repository.RolesRepository;

import com.github.backend.service.exception.CommonException;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.dto.mates.RequestSaveMateDto;
import com.github.backend.web.dto.users.*;
import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.RolesEntity;
import com.github.backend.web.entity.UserEntity;
import com.github.backend.web.entity.enums.ErrorCode;
import com.github.backend.web.entity.enums.Gender;
import com.github.backend.web.entity.enums.MateStatus;
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
    private final AESUtil aesUtil;
    private final String BEARER_TYPE = "Bearer";

    /*
     * UsernamePasswordAuthenticationToken을 통한 Spring Security인증 진행
     * 이후 tokenService에 userId값을 전달하여 토큰 생성
     * @param requestDTO
     * @return response
     */
    @Transactional
    public ResponseEntity<?> login(RequestLoginDto requestDTO, HttpServletResponse httpServletResponse) {
        UserEntity users = authRepository.findByUserId(requestDTO.getUserId()).isPresent() == false ? null : authRepository.findByUserId(requestDTO.getUserId()).get();
        MateEntity mates = mateRepository.findByMateId(requestDTO.getUserId()).isPresent() == false ? null : mateRepository.findByMateId(requestDTO.getUserId()).get();

        String rolesName = "";
        Map<String, String> response = new HashMap<>();

        if(users == null && mates == null){
          throw new CommonException("아이디 혹은 비밀번호가 틀렸습니다.", ErrorCode.BAD_REQUEST_RESPONSE);
        }
        try{
          // 1. ID(email)/PW 기반으로 AuthenticationToken 생성
          UsernamePasswordAuthenticationToken authenticationToken = requestDTO.toAuthentication();
          // 2. 실제 검증 로직
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);

          // 3. redis에 로그아웃 토큰이 있는 경우 삭제
          if (redisTemplate.opsForValue().get("logout: " + requestDTO.getUserId()) != null) {
            redisTemplate.delete("logout: " + requestDTO.getUserId());
          }

          String accessToken = "";
          String refreshToken = "";

          // 4. 유저, 메이트 인증 정보를 기반으로 JWT토큰 생성
          if(users != null){
            String isDeletedUser = users.getIsDeleted();

            if(!passwordEncoder.matches(requestDTO.getPassword(), users.getPassword()))
              throw new CommonException("아이디 혹은 비밀번호가 틀렸습니다.", ErrorCode.BAD_REQUEST_RESPONSE);

            if(isDeletedUser != null && isDeletedUser.equals("deleted"))
              throw new CommonException("해당 사용자는 탈퇴한 계정입니다.", ErrorCode.BAD_REQUEST_RESPONSE);

            if(users.isBlacklisted())
              throw new CommonException("해당 사용자는 관리자에게 문의하십시오. helpUAdmin@admin.com", ErrorCode.BAD_REQUEST_RESPONSE);

            accessToken = jwtTokenProvider.createAccessToken(1, users.getUserId());
            refreshToken = jwtTokenProvider.createRefreshToken(1, users.getUserId());
            rolesName = users.getRoles().getRolesName();


          }else if(mates != null){
            String isDeletedMate = mates.getIsDeleted();

            if(!passwordEncoder.matches(requestDTO.getPassword(), mates.getPassword()))
              throw new CommonException("아이디 혹은 비밀번호가 틀렸습니다.", ErrorCode.BAD_REQUEST_RESPONSE);

            if(isDeletedMate != null && isDeletedMate.equals("deleted"))
              throw new CommonException("해당 메이트는 탈퇴한 계정입니다.", ErrorCode.BAD_REQUEST_RESPONSE);

            if(mates.isBlacklisted())
              throw new CommonException("해당 메이트는 관리자에게 문의하십시오. helpUAdmin@admin.com", ErrorCode.BAD_REQUEST_RESPONSE);

            if(mates.getMateStatus().equals(MateStatus.PREPARING))
              throw new CommonException("관리자의 허가가 필요한 아이디 입니다.", ErrorCode.BAD_REQUEST_RESPONSE);

            accessToken = jwtTokenProvider.createAccessToken(2, mates.getMateId());
            refreshToken = jwtTokenProvider.createRefreshToken(2, mates.getMateId());
            rolesName = mates.getRoles().getRolesName();

          }

          // Redis에 token 적용
          redisTemplate.opsForValue().set(requestDTO.getUserId(), accessToken, Duration.ofHours(1L));
          redisTemplate.opsForValue().set("RF: " + requestDTO.getUserId(), refreshToken, Duration.ofHours(3L));

          httpServletResponse.addCookie(new Cookie("refresh_token", refreshToken));

          response.put("http_status", HttpStatus.OK.toString());
          response.put("message", "로그인 되었습니다");
          response.put("token_type", BEARER_TYPE);
          response.put("access_token", accessToken);
          response.put("refresh_token", refreshToken);
          response.put("roles", rolesName);
          return ResponseEntity.ok(response);

        }catch (BadCredentialsException e) {
          e.printStackTrace();
          response.put("message", "잘못된 자격 증명입니다");
          response.put("http_status", HttpStatus.UNAUTHORIZED.toString());
          return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

    /*
     * 유저 회원가입 로직
     *
     * @param requestSaveUserDto
     * @return CommonResponseDto
     */
    @Transactional
    public CommonResponseDto userSignup(RequestSaveUserDto requestSaveUserDto) {
      if (authRepository.existsByUserId(requestSaveUserDto.getUserId())) {
        throw new CommonException("이미 존재하는 아이디입니다.", ErrorCode.BAD_REQUEST_RESPONSE);
      }
      requestSaveUserDto.setPassword(passwordEncoder.encode(requestSaveUserDto.getPassword()));

      RolesEntity roles = rolesRepository.findById(requestSaveUserDto.getRoles()).orElseThrow(() -> new CommonException("권한이 존재하지 않습니다.", ErrorCode.UNAUTHORIZED_RESPONSE));

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

    /*
     * 메이트 회원가입 로직
     *
     * @param requestMateDto
     * @return CommonResponseDto
     */
    @Transactional
    public CommonResponseDto mateSignup(RequestSaveMateDto requestMateDto) {
        if (mateRepository.existsByMateId(requestMateDto.getMateId())) {
          throw new CommonException("이미 존재하는 아이디입니다.", ErrorCode.BAD_REQUEST_RESPONSE);
        }
        requestMateDto.setPassword(passwordEncoder.encode(requestMateDto.getPassword()));

        RolesEntity roles = rolesRepository.findById(requestMateDto.getRoles()).orElseThrow(() -> new CommonException("권한이 존재하지 않습니다.", ErrorCode.UNAUTHORIZED_RESPONSE));

        Gender gender;
        if (requestMateDto.getGender().equals("남자"))
          gender = Gender.MEN;
        else
          gender = Gender.WOMEN;

        // 주민등록번호 암호화
        String encryptNum = aesUtil.encrypt(requestMateDto.getRegistrationNum());

        MateEntity mate = MateEntity.builder()
                .mateId(requestMateDto.getMateId())
                .password(requestMateDto.getPassword())
                .email(requestMateDto.getEmail())
                .name(requestMateDto.getName())
                .phoneNumber(requestMateDto.getPhoneNumber())
                .address(requestMateDto.getAddress())
                .gender(gender)
                .roles(roles)
                .registrationNum(encryptNum)
                .isDeleted(null)
                .build();

        log.info("[build] mate = " + mate);
        mateRepository.save(mate);

       return CommonResponseDto.builder()
                .code(200)
                .message("메이트 회원가입이 완료되었습니다. (관리자의 허가가 필요합니다)")
                .success(true)
                .build();
    }

    /*
     * 메이트 회원가입 로직
     */
    @Transactional
    public void logout(String token){
      redisTemplate.opsForValue().set("logout : "+ jwtTokenProvider.getIdByToken(token), "logout", Duration.ofSeconds(600));
      redisTemplate.delete(jwtTokenProvider.getIdByToken(token));
      redisTemplate.delete("RF: " + jwtTokenProvider.getIdByToken(token));
    }

    // refresh token 재발급
    @Transactional
    public ResponseEntity<?> refresh(String token){
      String id = jwtTokenProvider.getIdByToken(token);
      String accessToken = checkByToken(id);
      if(redisTemplate.opsForValue().get("RF: " + id) != null){
        redisTemplate.opsForValue().set(id, accessToken, Duration.ofSeconds(3L));
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
