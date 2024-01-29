package com.github.backend.web.entity.enums;

public enum CareStatus {
  WAITING("대기 중"),
  IN_PROGRESS("진행 중"),
  HELP_DONE("도움 완료");

  private final String careStatus;

  CareStatus(String careStatus) {
    this.careStatus = careStatus;
  }
}
