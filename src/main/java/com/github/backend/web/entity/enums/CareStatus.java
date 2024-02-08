package com.github.backend.web.entity.enums;

public enum CareStatus {
  WAITING("대기 중"),
  IN_PROGRESS("진행 중"),
  HELP_DONE("도움 완료"),
  CANCEL("취소"),
  COMPLETE_PAYMENT("결제 완료"),
  INCOMPLETE_PAYMENT("결제 미완");

  private final String careStatus;

  CareStatus(String careStatus) {
    this.careStatus = careStatus;
  }
}
