package com.github.backend.web.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseMyInfoDto {
    private Long cid;
    private String name;
    private String id;
    private String email;
    private String phoneNumber;
    @Builder
    public ResponseMyInfoDto(Long cid,String name, String id, String email, String phoneNumber){
      this.cid = cid;
      this.name = name;
      this.id = id;
      this.email = email;
      this.phoneNumber = phoneNumber;
    }
}
