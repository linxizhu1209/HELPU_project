package com.github.backend.config.security;

import com.github.backend.repository.AuthRepository;
import com.github.backend.repository.MateRepository;
import com.github.backend.service.auth.UserService;
import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.UserEntity;
import com.github.backend.web.entity.custom.CustomMateDetails;
import com.github.backend.web.entity.custom.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    @Value("${spring.jwt.secret}")
    private String secretKey;
    private final String AUTHORITIES_KEY = "roles";
    private final Long ACCESS_TOKEN_EXPIRED_TIME = 60 * 60 * 1000L;
    private final Long REFRESH_TOKEN_EXPIRED_TIME = 7 * 24 * 60 * 60 * 1000L;

    private final UserService userService;
    private final AuthRepository authRepository;
    private final MateRepository mateRepository;
    @PostConstruct
    protected void init(){
      secretKey  = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    // accessToken과 refreshToken을 생성함
    // @param subject
    // @return TokenDto
    public String createToken(String userId, String role, Long expireLength) {
      Claims claims = Jwts.claims().setSubject(userId); // payload부분에 들어갈 정보 조각
      claims.put(AUTHORITIES_KEY, role);
      Date now = new Date();
      Date validity = new Date(now.getTime() + expireLength);
      return Jwts.builder()
              .setClaims(claims)
              .setIssuedAt(now)
              .setExpiration(validity)
              .signWith(SignatureAlgorithm.HS256, secretKey)
              .compact();
    }

    private String getUserId(String token) {
      return Jwts.parser()
              .setSigningKey(secretKey)
              .parseClaimsJws(token)
              .getBody()
              .getSubject();
    }
    public Authentication getAuthentication(String token){
      UserDetails userDetails = userService.loadUserByUsername(this.parseClaims(token));
      Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
      log.info("Authentication authorities: " + authentication.getAuthorities());
      return authentication;
    }

    public String createAccessToken(Integer check, String id){
      if(check == 1){
        Optional<UserEntity> isUser = authRepository.findByUserId(id);
        return isUser.map(user -> createToken(id, user.getRoles().getRolesName(),ACCESS_TOKEN_EXPIRED_TIME))
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원 입니다"));
      }else{
        Optional<MateEntity> isMate = mateRepository.findByMateId(id);
        return isMate.map(mate -> createToken(id, mate.getRoles().getRolesName(),ACCESS_TOKEN_EXPIRED_TIME))
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 메이트 입니다"));
      }

    }
    //리프레쉬 토큰 생성
    public String createRefreshToken(Integer check, String id){
        if(check == 1){
          Optional<UserEntity> isUser = authRepository.findByUserId(id);
          return isUser.map(user -> createToken(id, user.getRoles().getRolesName(),REFRESH_TOKEN_EXPIRED_TIME))
                  .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원 입니다"));
        }else{
          Optional<MateEntity> isMate = mateRepository.findByMateId(id);
          return isMate.map(mate -> createToken(id, mate.getRoles().getRolesName(),REFRESH_TOKEN_EXPIRED_TIME))
                  .orElseThrow(() -> new NoSuchElementException("존재하지 않는 메이트 입니다"));
        }
    }

    public boolean validationToken(String token) { // 토큰 유효성 검사
        try {
          Jws<Claims> claimsJws = Jwts.parserBuilder()
                  .setSigningKey(secretKey)
                  .build()
                  .parseClaimsJws(token);
          return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.debug("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.debug("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.debug("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.debug("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private String parseClaims(String accessToken) {
      return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody().getSubject();
    }

    //토큰에서 이메일 값 추출
    public String getIdByToken(String token) {
      // JWT 토큰을 디코딩하여 페이로드를 얻기
      Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
      // "userId" 클레임의 값을 얻기
      return claims.isEmpty() ? null : claims.get("sub", String.class);
    }
}
