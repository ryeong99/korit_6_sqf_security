package com.study.springsecurity.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(value = 2) // 우선순위
public class TestAspect {

    @Pointcut("execution(* com.study.springsecurity.service.TestService.aop*(..))")
    private void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        System.out.println("전처리");
        Object result = proceedingJoinPoint.proceed();
        System.out.println("후처리");

        return result;
    }
}
