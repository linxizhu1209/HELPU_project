package com.github.backend.web.dto.mates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MainPageDto {
private int waitingCount;
}
