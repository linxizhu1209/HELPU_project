package com.github.backend.web.dto.response;

import com.github.backend.service.exception.CommonException;
import com.github.backend.web.entity.enums.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//응답 포맷 설정
@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private String code;
    private HttpStatus status;

    public ErrorResponse (Throwable e){
      if (e instanceof CommonException) {
        CommonException ex = (CommonException)e ;
        this.code = ex.getErrCode();
        this.message = ex.getErrMsg();
        this.status = ex.getStatus();
      } else {
        CommonException ex = new CommonException(ErrorCode.FAIL_RESPONSE);
        this.code = ex.getErrCode();
        this.message = ex.getErrMsg();
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
      }
    }

    public ResponseEntity build() {
        ResponseEntity responseEntity = new ResponseEntity<>(this, status);
        return responseEntity;
    }
}
