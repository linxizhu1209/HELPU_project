package com.github.backend.web.entity.enums;

public enum MateStatus {
  PREPARING("인증 전"),
  COMPLETE("인증 완료"),
  FAILED("인증 실패");

  private final String mateStatus;

  MateStatus(String mateStatus) {
    this.mateStatus = mateStatus;
  }
}
