package com.github.backend.web.dto.users;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

// 로그인 시 사용
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestLoginDto {
    @Schema(description = "아이디", name = "user_id", example = "test1234")
    private String userId;
    @Schema(description = "비밀번호", name = "password", example = "qwer1234")
    private String password;

    // Authentication을 implements 한 AbstractAuthenticationToken의 하위 클래스
    // username이 Principal의 역할을 하고, password가 Credential의 역할
    // 첫 번째 생성자는 인증 전의 객체를 생성하고, 두 번째 생성자는 인증이 완료된 객체를 생성
    public UsernamePasswordAuthenticationToken toAuthentication() {
      return new UsernamePasswordAuthenticationToken(userId, password);
    }
}
