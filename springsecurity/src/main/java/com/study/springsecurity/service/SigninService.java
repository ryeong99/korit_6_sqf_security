package com.study.springsecurity.service;

import com.study.springsecurity.domain.entity.User;
import com.study.springsecurity.dto.request.ReqSigninDto;
import com.study.springsecurity.dto.request.ReqSignupDto;
import com.study.springsecurity.dto.response.RespJwtDto;
import com.study.springsecurity.repository.UserRepository;
import com.study.springsecurity.sequrity.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SigninService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    public RespJwtDto signin(ReqSigninDto dto) {
        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("사용자 정보를 다시 입력하세요.")
        );
        if (!passwordEncoder.matches(dto.getPassword(),user.getPassword())) {
            throw new BadCredentialsException("사용자 정보를 다시 입력하세요.");
        }

        return RespJwtDto.builder()
                .accessToken(jwtProvider.generateUserToken(user))
                .build();
    }
}
