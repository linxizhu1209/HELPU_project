package com.github.backend.web.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 에러코드와 메시지를 담은 열거형
@Getter
@AllArgsConstructor
public enum ErrorCode {
    SUCCESS_RESPONSE("200", "Successful", HttpStatus.OK),
    FAIL_RESPONSE("404", "Not Found Error", HttpStatus.NOT_FOUND),
    BAD_REQUEST_RESPONSE("400", "Bad Request Error", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_RESPONSE("401", "Unauthorized Error", HttpStatus.UNAUTHORIZED),
    INTERNAL_FAIL_RESPONSE("500", "Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);

    private String errCode;
    private String errMsg;
    private HttpStatus status;
}
