package com.github.backend.web.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/*
* timestamp 형식으로 전달하기 위한 converter
* */
public class TimestampUtil {

  public static long convertLocalDateTimeToTimestamp(LocalDateTime localDateTime) {
      return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
  }
}
