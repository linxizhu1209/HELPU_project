package com.github.backend.web.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 에러코드와 메시지를 담은 열거형
@Getter
@AllArgsConstructor
public enum ErrorCode {
    SUCCESS_RESPONSE("200", "Successful response", HttpStatus.OK),
    FAIL_RESPONSE("404", "Fail response", HttpStatus.INTERNAL_SERVER_ERROR);

    private String errCode;
    private String errMsg;
    private HttpStatus status;
}
