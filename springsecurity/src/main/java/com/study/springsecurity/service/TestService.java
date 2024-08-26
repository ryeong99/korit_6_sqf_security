package com.study.springsecurity.service;

import com.study.springsecurity.aspect.annotation.Test2Aop;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    public String aopTest() {
        System.out.println("AOP TEST");
        return "AOP TEST";
    }

    @Test2Aop
    public void aopTest2(String name, int age) {
        System.out.println("이름:" + name);
        System.out.println("나이:" + age);
        System.out.println("AOP TEST 2");
    }

    @Test2Aop
    public void aopTest3(String phone, String address) {
        System.out.println("AOP TEST 3");
    }
}
