package com.github.backend.web.advice;

import com.github.backend.service.exception.*;
import com.github.backend.web.dto.response.ErrorResponse;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException nfe){
      log.error("Client 요청 이후 DB 검색 중 에러로 다음처럼 출력합니다." + nfe.getMessage());
      return nfe.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(NotAcceptException.class)
    public String handleNotAcceptException(NotAcceptException nae){
      log.error("Client 요청이 모종의 이유로 거부됩니다. " + nae.getMessage());
      return nae.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidValueException.class)
    public String handleInvalidValueException(InvalidValueException ive){
      log.error("Client 요청에 문제가 있어 다음처럼 출력합니다. " + ive.getMessage());
      return ive.getMessage();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ImageUploadException.class)
    public String handleImageUploadException(ImageUploadException iue){
      log.error("이미지 업로드 중에 문제가 발생했습니다. " + iue.getMessage());
      return iue.getMessage();
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity handleCommonException(CommonException e) {
      ErrorResponse res = new ErrorResponse(e);
      return res.build();
    }
}
