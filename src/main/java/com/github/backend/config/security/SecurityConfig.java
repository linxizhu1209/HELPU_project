package com.github.backend.config.security;

import com.github.backend.config.security.handler.JwtAccessDeniedHandler;
import com.github.backend.config.security.handler.JwtAuthenticationEntryPoint;
import com.github.backend.config.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    // 인증은 필요없는 api
    private final String[] PERMIT_URL = {
            "/",
            "/**",
            "/auth/**",
            "/css/**","/images/**","/js/**","/favicon.ico","/h2-console/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/api-docs/**",
            "/v3/**"
    };

    // 관리자 권한이 필요한 api
    private final String[] ADMIN_URL = {
            "/api/admin/**"
    };

    // 사용자 페이지 api
    private final String[] USER_URL = {
            "/api/user/**"
    };

    // 메이트 페이지 api
    private final String[] MATE_URL = {
            "/api/mate/**"
    };


  @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","PUT","POST","PATCH","DELETE","OPTIONS"));
        corsConfiguration.setMaxAge(1000L*60*60);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http
              .csrf((csrf) -> csrf.disable())  //csrf설정 끔
              // cors 설정
              .cors((cors) -> {
                cors.configurationSource(corsConfigurationSource());
              })
              .sessionManagement((session) ->
                      session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
              )  //세션은 stateless방식
              .formLogin((auth) -> auth.disable())// formLogin 비활성화
              .rememberMe((remember) -> remember.disable())

              //예외처리
              .exceptionHandling((exception) ->
                      exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
              )
              .exceptionHandling((exception) -> exception.accessDeniedHandler(jwtAccessDeniedHandler))

              //인증 진행할 uri설정
              .authorizeHttpRequests((auth) ->
                      auth
                              .requestMatchers(PERMIT_URL).permitAll()
                              .requestMatchers(ADMIN_URL).hasRole("MASTER")
                              .requestMatchers(USER_URL).hasRole("USER")
                              .requestMatchers(MATE_URL).hasRole("MATE")
              )
              //jwt필터를 usernamepassword인증 전에 실행
              .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);


      log.info("securityConfig");
      return http.build();
    }
}
