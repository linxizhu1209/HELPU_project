package com.github.backend.web.entity.enums;

public enum MateCareStatus {
    IN_PROGRESS("진행 중"),
    HELP_DONE("도움 완료"),
    CANCEL("취소");

    private final String mateCareStatus;

    MateCareStatus(String mateCareStatus) {
        this.mateCareStatus = mateCareStatus;
    }
}
