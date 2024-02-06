package com.github.backend.web.entity.custom;

import com.github.backend.web.entity.MateEntity;
import com.github.backend.web.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Builder
@ToString
@Getter
public class CustomMateDetails implements UserDetails {
    private MateEntity mate;

    public CustomMateDetails(MateEntity mate) {
      this.mate = mate;
    }

    // getAuthorities 반환값으로 Collection 처리
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      Collection<GrantedAuthority> collections = new ArrayList<>();
      collections.add(() -> String.valueOf(mate.getRoles().getRolesName()));
      return collections;
    }

    @Override
    public String getPassword() {
      return mate.getPassword();
    }

    @Override
    public String getUsername() {
      return mate.getMateId();
    }

    /* 계정 만료 여부
     * true :  만료 안됨
     * false : 만료
     */
    @Override
    public boolean isAccountNonExpired() {
      return true;
    }

    /* 계정 잠김 여부
     * true : 잠기지 않음
     * false : 잠김
     */
    @Override
    public boolean isAccountNonLocked() {
      return true;
    }

    /* 비밀번호 만료 여부
     * true : 만료 안 됨
     * false : 만료
     */
    @Override
    public boolean isCredentialsNonExpired() {
      return true;
    }

    /* 사용자 활성화 여부
     * true : 활성화 됨
     * false : 활성화 안 됨
     */
    @Override
    public boolean isEnabled() {
      return true;
    }
}
