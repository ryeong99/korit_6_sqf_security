package com.study.springsecurity.controller;

import com.study.springsecurity.aspect.annotation.ParamsAop;
import com.study.springsecurity.aspect.annotation.ValidAop;
import com.study.springsecurity.dto.request.ReqSigninDto;
import com.study.springsecurity.dto.request.ReqSignupDto;
import com.study.springsecurity.service.SigninService;
import com.study.springsecurity.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private SignupService signupService;

    @Autowired
    private SigninService signinService;

    @ValidAop
    @ParamsAop
    @PostMapping("/signup")
    public ResponseEntity<?> singup(@Valid @RequestBody ReqSignupDto dto, BindingResult bindingResult) {
       return ResponseEntity.created(null).body(signupService.signup(dto));
    }

    @ValidAop
    @PostMapping("/signin")
    public ResponseEntity<?> signin(
            @Valid @RequestBody ReqSigninDto dto,
            BeanPropertyBindingResult bindingResult) {
        return ResponseEntity.ok().body(signinService.signin(dto));
    }

}
