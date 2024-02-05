package com.github.backend.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
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
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {
    private final Key encodeKey;
    private final String AUTHORITIES_KEY = "roles";
    private final Long ACCESS_TOKEN_EXPIRED_TIME = 60 * 60000 * 1000L;
    private final Long REFRESH_TOKEN_EXPIRED_TIME = 7000 * 24 * 60 * 60 * 1000L;

    private final UserDetailsService userDetailsService;

    public JwtTokenProvider(@Value("${spring.jwt.secret}")String secretKey, UserDetailsService userDetailsService){
      this.userDetailsService = userDetailsService;
      byte[] keyBytes = Base64.getDecoder().decode(secretKey);
      this.encodeKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Authentication authentication, String roles) {
      String token = createToken(authentication, roles, ACCESS_TOKEN_EXPIRED_TIME);
      return token;
    }

    public String createRefreshToken(Authentication authentication, String roles) {
      String token = createToken(authentication, roles, REFRESH_TOKEN_EXPIRED_TIME);
      return token;
    }

    // accessToken과 refreshToken을 생성함
    // @param subject
    // @return TokenDto
    public String createToken(Authentication authentication, String role, Long expireLength) {
      Claims claims = Jwts.claims().setSubject(authentication.getName()); // payload부분에 들어갈 정보 조각
      claims.put(AUTHORITIES_KEY, role);
      Date now = new Date();
      Date validity = new Date(now.getTime() + expireLength);
      return Jwts.builder()
              .setClaims(claims)
              .setIssuedAt(now)
              .setExpiration(validity)
              .signWith(encodeKey, SignatureAlgorithm.HS256)
              .compact();
    }

  // UsernamePasswordAuthenticationToken으로 보내 인증된 유저인지 확인
  //     * @param accessToken
  //     * @return Authentication
  //     * @throws ExpiredJwtException
  public Authentication getAuthentication(String accessToken) throws ExpiredJwtException{
    // 토큰 복호화 : JWT의 body
    Claims claims = parseClaims(accessToken);
    if(claims.get(AUTHORITIES_KEY) == null) {
      throw new RuntimeException("권한 정보가 없는 토큰입니다.");
    }

    // 클레임에서 권한 정보 가져오기
    Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

    // UserDetails 객체를 만들어서 Authentication 리턴
//        UserDetails principal = new User(claims.getSubject(),"", authorities);
    UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
    return new UsernamePasswordAuthenticationToken(userDetails, accessToken, authorities);
  }


    public boolean validationToken(String token) { // 토큰 유효성 검사
        try {
          Jws<Claims> claimsJws = Jwts.parserBuilder()
                  .setSigningKey(encodeKey)
                  .build()
                  .parseClaimsJws(token);
          return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
      try{
        return Jwts.parserBuilder().setSigningKey(encodeKey).build().parseClaimsJws(accessToken).getBody();
      }catch (ExpiredJwtException e) {
        return e.getClaims();
      }
    }
}
