package com.github.backend.config.security.filter;

import com.github.backend.config.security.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final String PREFIX = "Bearer";
    private final String AUTHORIZATION_HEADER = "Authorization";

    // 현재 실행중인 스레드(Security Context)에 Access Token으로부터 뽑아온 인증 정보를 저장.
    // SecurityContext는 어디서든 접근 가능한데, 정상적으로 Filter를 통과하여 Controller에 도착한다면, SecurityContext내부에 User의 username이 있다는 것이 보장
    // 실제 필터링 로직은 doFilterInternal에서 수행,
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      String requestURI = request.getRequestURI();

      // 1. Request Header 에서 토큰을 꺼냄
      String jwt = resolveToken(request);

      // 2. validateToken 으로 토큰 유효성 검사, 정상 토큰이면 해당 토큰으로 Authentication을 가져와서 SecurityContext에 저장
      if(StringUtils.hasText(jwt) && jwtTokenProvider.validationToken(jwt)) {
        Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);

      }else {
        log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
      }

      filterChain.doFilter(request, response);
    }

    //Header에서 Authorization부분을 추출할 때 Type이 Bearer인지 확인 후, 일치한다면 JWT부분만 추출하여 doFilter에 제공
    private String resolveToken(HttpServletRequest request) {
      String token = request.getHeader(AUTHORIZATION_HEADER);
      if(StringUtils.hasText(token) && token.startsWith(PREFIX)) {
        return token.substring(7);    // "Bearer "를 뺀 값, 즉 토큰 값
      }

      return null;
    }
}
