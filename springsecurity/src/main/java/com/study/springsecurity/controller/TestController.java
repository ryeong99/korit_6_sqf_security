package com.study.springsecurity.controller;

import com.study.springsecurity.sequrity.principal.PrincipalUser;
import com.study.springsecurity.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public ResponseEntity<?> get() {

        System.out.println(testService.aopTest());
//        testService.aopTest2("김준일",31);
//        testService.aopTest3("010-9988-1916","동래");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        return ResponseEntity.ok(principalUser);
    }
}
