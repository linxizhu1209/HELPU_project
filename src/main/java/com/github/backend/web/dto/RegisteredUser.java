package com.github.backend.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisteredUser {

private String username;
private int userAge;
private String userGender;

}
