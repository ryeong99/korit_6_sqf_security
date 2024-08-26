package com.study.springsecurity.sequrity.filter;

import com.study.springsecurity.domain.entity.User;
import com.study.springsecurity.repository.UserRepository;
import com.study.springsecurity.sequrity.jwt.JwtProvider;
import com.study.springsecurity.sequrity.principal.PrincipalUser;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAccessTokenFilter extends GenericFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // header에 Authorization이라는 키값으로 데이터가 넘어옴
        // PostMan에서 Authorization탭에 보면 Auth Type이 Bearer Token이 있는데 여기다가 Jwt토큰을 넣어준다.(클라이언트 요청 시)
        // 그럼 Headers에 Authorization Key값이 생기고 value는 Jwt토큰 값이 암호화되서 들어간다
        String bearerAccessToken = request.getHeader("Authorization");

//        System.out.println(bearerAccessToken);
        /*
            출력결과
            Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImV4cCI6MTcyMjY4MTMwMX0.Ine2WI4XbmZ8V6W8vBn2jK08XBP0LdHmKMHMMIFclM0
            앞에 붙어 있는 Bearer는 이 값이 Jwt토큰값이라는 것을 티를 내기 위해 붙여줌
            프론트엔드에서 요청을 날릴 때 앞에 Bearer을 붙여줘야함(형식: Bearer 토큰값)
        */

        // token검증을 할때는 Bearer을 제거해줘야 함
        if(bearerAccessToken != null) {
            String accessToken = jwtProvider.removeBearer(bearerAccessToken);  // 암호화 된 토큰값이므로 복호화해야함
            Claims claims = null;
            try {
                claims = jwtProvider.parseToken(accessToken);
            } catch (Exception e) {  // 예외 발생 시 다음 필터로 넘어감, 토큰값이 유효하지 않기 때문
                filterChain.doFilter(servletRequest, servletResponse);
                return;  // doFilter의 후처리는 실행이 되면 안되기 때문에 return을 걸어서 메서드 실행을 중지시킴
            }

            long userId = ((Integer)claims.get("userId")).longValue();
            Optional<User> optionalUser = userRepository.findById(userId);
            if(optionalUser.isEmpty()) {  // 토큰은 존재하지만 계정이 삭제된 경우
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            PrincipalUser principalUser = optionalUser.get().toPrincipalUser();
            // principal 객체는 무조건 들어가줘야 하고 credentials는 null이어도 상관없음
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalUser, principalUser.getPassword(), principalUser.getAuthorities());  // principal 객체, 서명
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }


        /*
            - Spring Security에서는 SecurityContextHolder에 Context객체에 Authentication값이 들어가 있어야 로그인이 됬다고 인식
            - UsernamePasswordAuthenticationFilter은 SecurityContextHolder에 Context객체에 있는 Authentication값을
              보고 있으면 인증할 필요가 없다고 판단하고 다음 필터로 넘어감
            - 폼 로그인은 바로 UsernamePasswordAuthenticationFilter에서 검증하게 되고 다음 필터로 넘어감
         */

        filterChain.doFilter(servletRequest, servletResponse);  // 다음 필터로 넘어야가 되기 때문에 무조건 넣어줘야 함
    }
}
