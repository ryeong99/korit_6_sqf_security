package com.study.springsecurity.config;

import com.study.springsecurity.sequrity.filter.JwtAccessTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity //우리가 만든 SecurityConfig를 적용시키겠다.
@Configuration //(필수) IOC 컨테이너에 객체생성
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAccessTokenFilter jwtAccessTokenFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().disable();
        http.httpBasic().disable();

        http.cors();
        http.csrf().disable();
        // 위조방지 스티커(토큰)

//        http.sessionManagement().disable();
        // 스프링 시큐리티가 세션을 생성하지않고 기존의 몯느 생성을 요하지않는다.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 스프링 시큐리티가 세션을 생성하지 않겠다. 기존의 세션을 완전히 사용하지 않겠다는 것은 아님.
        // JWT 등의 토큰 인증박식을 사용할 때 설정하는 것
        http.authorizeRequests()
                .antMatchers("/auth/**","/h2-console/**" )
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .headers()
                .frameOptions()
                .disable();

        http.addFilterBefore(jwtAccessTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
