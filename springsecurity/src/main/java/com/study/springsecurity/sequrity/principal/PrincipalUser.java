package com.study.springsecurity.sequrity.principal;

import com.study.springsecurity.domain.entity.Role;
import com.study.springsecurity.domain.entity.Role;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Data
public class PrincipalUser implements UserDetails {
    private Long userId;
    private String username;
    private String password;
    private Set<Role> roles;

    /*
        Security가 User라는 객체를 가질 수는 없음
        인증된 사용자를 Authentication이라는 객체를 통해서 관리
        Authentication 안에 Principal이 들어있고, UserDetails와 OAuth를 관리
    */

    // 권한들을 가지고 있음
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Set<GrantedAuthority> authorities = new HashSet<>();
//        for(Role role : roles) {
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
//        }
//        return authorities;

        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
    }

    // 패스워드와 아이디는 폼 로그인 할때만 사용함
    // @Data를 사용하고 있기 때문에 Getter를 지워줘도 상관없음
//    @Override
//    public String getPassword() {
//        return "";
//    }
//
//    @Override
//    public String getUsername() {
//        return "";
//    }

    /*
        아래 메서드들 중 하나라도 false면 인증 안됨
     */

    // 기간이 만료된 계정들
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 잠긴 계정들
    // ex. 비밀번호를 5번 이상 틀린 경우
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 인증이 만료된 경우, 임시계정들을 사용하는 용도로 사용하기도 함
    // ex. 1년 이상 로그인이 없는 경우
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화 되었는지
    // ex. 이메일 인증을 아직 거치지 않았을 경우 unabled
    @Override
    public boolean isEnabled() {
        return true;
    }
}
